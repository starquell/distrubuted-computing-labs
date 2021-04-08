/// В одному лісі живуть n бджіл і один ведмідь, які
/// використовують один горщик меду, місткістю N ковтків. Спочатку горщик порожній. Поки
/// горщик не наповниться, ведмідь спить. Як тільки горщик заповнюється, ведмідь прокидається і
/// з'їдає весь мед, після чого знову засинає. Кожна бджола багаторазово збирає по одній порції
/// меду і кладе його в горщик. Бджола, яка приносить останню порцію меду, будить
/// ведмедя. Створить багатопоточний додаток, моделюючий поведінку бджіл і ведмедя.

package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	MaxBites = 15
	BeesN = 6
	SimulationTime = time.Second * 120
)

type Basket struct {
	currentBites int
	maxBites int
	mut sync.Mutex
}

func makeDefaultBasket() Basket {
	return Basket{
		currentBites: 0,
		maxBites: MaxBites,
	}
}

func (basket *Basket) addBite() {
	basket.currentBites++
}

func (basket *Basket) full() bool {
	return basket.currentBites == basket.maxBites
}

func eatAll(basket *Basket) {
	time.Sleep(100 * time.Duration(basket.currentBites) * time.Millisecond)
	basket.currentBites = 0
	fmt.Println("Nyam nyam")
}

func main() {
	rand.Seed(time.Now().UnixNano())
	basket := makeDefaultBasket()

	for i := 0; i < BeesN; i++ {
		go func(beeNumber int) {
			for {
				time.Sleep(time.Duration(rand.Intn(3) + 1) * time.Second)
				basket.mut.Lock()
				basket.addBite()
				fmt.Printf("Bee #%d brought honey, basket is filled by %d%% \n", beeNumber, int(100 * float32(basket.currentBites) / float32(basket.maxBites)))
				if basket.full() {
					eatAll(&basket)
				}
				basket.mut.Unlock()
			}
		}(i)
	}
	time.Sleep(SimulationTime)
}
