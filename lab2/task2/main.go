package main

import (
	"fmt"
	"math/rand"
	"time"
)

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///  Темної-темної ночі прапорщики Іванов, Петров і Нечипорчук займаються розкраданням військового майна зі складу рідної військової частини.
///  Будучи розумними людьми і відмінниками бойової та стройової підготовки, прапорщики ввели поділ праці:
///  		Іванов виносить майно зі складу, Петров вантажить його в вантажівку, а Нечипорчук підраховує вартість майна.
///  Потрібно скласти багатопоточний додаток, що моделює діяльність прапорщиків.
///  При вирішенні використати парадигму «виробник-споживач» з активним очікуванням.
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

type Property struct {
	name string
	cost int
}

func randomProperty() Property {
	rand.Seed(time.Now().UnixNano())
	var properties = [...]Property{
		{"AKM", 9200},
		{"Rations", 70},
		{"Flashlight", 830},
		{"RPG", 34000},
		{"Old socks", 2000},
		{"Bread", 10},
		{"Pants", 40},
		{"Shoes", 1650},
		{"Kolbasa", 60},
		{"Eight hryvnas", 8},
		{"Knife", 300},
	}
	return properties[rand.Intn(len(properties))]
}

type Warehouse []Property

func steal(warehouse Warehouse, to chan Property) {
	for len(warehouse) != 0 {
		time.Sleep(500 * time.Millisecond)
		stolen := warehouse[len(warehouse)-1]
		warehouse = warehouse[:len(warehouse)-1]
		to <- stolen
		fmt.Printf("Ivanov: I have stolen %s.\n", stolen.name)
	}
	close(to)
}

func load(from chan Property, to chan Property) {
	for property := range from {
		time.Sleep(400 * time.Millisecond)
		to <- property
		fmt.Printf("Petrov: I have loaded %s into track\n", property.name)
	}
	close(to)
}

func calculate(from chan Property) int {
	var sum int = 0
	for property := range from {
		time.Sleep(300 * time.Millisecond)
		sum += property.cost
		fmt.Printf("Nechyporuk: Received %s, costs %d hryvnas, total - %d\n", property.name, property.cost, sum)
	}
	return sum
}

func main() {
	warehouse := make(Warehouse, 10)
	for i := range warehouse {
		warehouse[i] = randomProperty()
	}
	fromIvanov := make(chan Property)
	fromPetrov := make(chan Property)
	go steal(warehouse, fromIvanov)
	go load(fromIvanov, fromPetrov)
	totalStolen := calculate(fromPetrov)
	fmt.Printf("Operation completed, we have stolen property for %d hryvnas\n", totalStolen)
}
