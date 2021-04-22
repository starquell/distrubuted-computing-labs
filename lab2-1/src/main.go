package main

import (
	"encoding/xml"
	"fmt"
	"github.com/Existed/libxml2"
	"github.com/Existed/libxml2/xsd"
	"io/ioutil"
)

func printDelim() {
	fmt.Println("***************************************************************************")
}

func demo(hr *HumanResources) {

	printDelim()
	fmt.Println("Adding new department...")
	cleaningDep := hr.AddDepartment("Cleaning")
	fmt.Printf("New deparment - %v\n", cleaningDep)

	printDelim()

	fmt.Println("Adding another department...")
	engDep := hr.AddDepartment("Engineers")
	fmt.Printf("New department - %v\n", engDep)

	printDelim()

	fmt.Printf("Adding new employee to %s department...\n", engDep.Name)
	emp := hr.AddEmployee(engDep, "Ostap", 0.85)
	fmt.Printf("New employee - %v\n", emp)

	printDelim()

	fmt.Printf("Finding new employee by ID (%d)...\n", emp.ID)
	emp = hr.FindEmployeeByID(emp.ID)		// genius
	fmt.Printf("Found employee: %v\n", emp)

	printDelim()

	fmt.Println("Renaming found employee to \"Oleksii\"...")
	emp.Name = "Oleksii"
	fmt.Printf("Employee: %v\n", emp)

	printDelim()

	fmt.Println("Renaming Oleksii`s departament to \"Volunteers\" ...")
	engDep.Name = "Volunteers"
	fmt.Printf("Deparment: %v\n", engDep)

	printDelim()

	fmt.Println("Eliminating Cleaning department ...")
	hr.RemoveByID(cleaningDep.ID)

	printDelim()

	fmt.Println(hr)
}

func validate(xml []byte) {
	schema, err := xsd.ParseFromFile("resources/HR.xsd")
	if err != nil {
		panic(err)
	}
	defer schema.Free()
	doc, err := libxml2.Parse(xml)
	if err != nil {
		panic(err)
	}
	if errs := schema.Validate(doc); errs != nil {
		for _, e := range errs.Error() {
			 println(e)
		}
	} else {
		println("XML validation successful!")
	}
}

func main() {
	var hr HumanResources
	fileContent, _ := ioutil.ReadFile("resources/HR.xml")
	validate(fileContent)
	xml.Unmarshal(fileContent, &hr)

	demo(&hr)

 	xmled, _ := xml.MarshalIndent(hr, "  ", "    ")
 	ioutil.WriteFile("resources/HR.xml", xmled, 0644)
}
