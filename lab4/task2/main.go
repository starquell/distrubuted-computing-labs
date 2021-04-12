package main

import (
	"bufio"
	"fmt"
	"math/rand"
	"os"
	"strings"
	"sync"
	"time"
)

type PlantStatus int

const (
	Normal PlantStatus = iota
	Sick
)

func (plant PlantStatus) String() string {
	return [...]string{"🌱", "🍂"}[plant]
}

type Garden struct {
	length uint
	plants [][]PlantStatus
	mut    sync.RWMutex
}

func NewGarden(length uint) *Garden {
	garden := Garden{
		length: length,
		plants: make([][]PlantStatus, length),
	}
	for i := range garden.plants {
		garden.plants[i] = make([]PlantStatus, length)
		for j := range garden.plants[i] {
			garden.plants[i][j] = PlantStatus(rand.Int() % 2)
		}
	}
	return &garden
}

func (garden *Garden) String() string {
	var res strings.Builder
	garden.mut.RLock()
	defer garden.mut.RUnlock()

	for i := range garden.plants {
		for j := range garden.plants[i] {
			res.WriteString(garden.plants[i][j].String() + " ")
		}
		res.WriteByte('\n')
	}
	return res.String()
}

func treatPlants(garden *Garden) {
	for {
		for i := range garden.plants {
			for j := range garden.plants[i] {
				garden.mut.Lock()
				garden.plants[i][j] = PlantStatus(Normal)
				garden.mut.Unlock()
				time.Sleep(200 * time.Millisecond)
			}
		}
	}
}

func doNature(garden *Garden) {
	for {
		time.Sleep(time.Second)
		garden.mut.Lock()
		plant := &garden.plants[rand.Intn(int(garden.length))][rand.Intn(int(garden.length))]
		*plant = PlantStatus(1 - int(*plant))
		garden.mut.Unlock()
	}
}

func main() {
	rand.Seed(time.Now().Unix())
	garden := NewGarden(8)

	go treatPlants(garden)
	go doNature(garden)

	go func() { /// Prints garden status to stdout
		for {
			fmt.Printf("%v\n\n", garden)
			time.Sleep(1500 * time.Millisecond)
		}
	}()

	go func() {
		file, err := os.Create("garden.txt")
		if err != nil {
			panic(err)
		}
		defer file.Close()
		w := bufio.NewWriter(file)
		for {
			garden.mut.RLock()
			_, err := fmt.Fprintf(w, "%v\n\n", garden)
			garden.mut.RUnlock()
			if err != nil {
				panic(err)
			}
			w.Flush()
			time.Sleep(4 * time.Second)
		}
	}()
	time.Sleep(time.Hour) /// WaitGroup would be better
}
