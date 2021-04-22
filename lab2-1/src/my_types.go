package main

import (
	"encoding/xml"
	"fmt"
)

type Employee struct {
	XMLName  xml.Name `xml:"employee"`
	ID       int64    `xml:"id"`
	Name     string   `xml:"name"`
	Velocity float32  `xml:"velocity"`
}
func (emp *Employee) String() string {
	return fmt.Sprintf("[id = %v] Employee %s, velocity - %.2f.", emp.ID, emp.Name, emp.Velocity)
}

type Department struct {
	XMLName    xml.Name    `xml:"department"`
	ID         int64       `xml:"id"`
	Name       string      `xml:"name"`
	Employees  []Employee   `xml:"employees>employee"`
}

func (dep *Department) String() string {
	return fmt.Sprintf("[id = %v] Departament %s, has %d employee(s).", dep.ID, dep.Name, len(dep.Employees))
}

type HumanResources struct {
	XMLName 	  xml.Name     `xml:"hr"`
	CurrentMaxID  int64        `xml:"maxID"`
    Departments   []Department  `xml:"departments>department"`
}

func NewHR() HumanResources {
	return HumanResources{
		CurrentMaxID: -1,
		Departments: make([]Department, 0),
	}
}
func (hr *HumanResources) incID() int64 {
	hr.CurrentMaxID++
	return hr.CurrentMaxID
}

func (hr *HumanResources) String() string {
	return fmt.Sprintf("Info from HR: exists %d departament(s) with %d employee(s) in total.",
		               len(hr.Departments), hr.EmployeesNumber())
}

func (hr *HumanResources) AddDepartment(departmentName string) *Department {
	hr.Departments = append(hr.Departments, Department{
		ID:        hr.incID(),
		Name:      departmentName,
		Employees: make([]Employee, 0),
	})
	return &hr.Departments[len(hr.Departments) - 1]
}

func (hr *HumanResources) AddEmployee(dep *Department, name string, velocity float32) *Employee {
	dep.Employees = append(dep.Employees, Employee{
		ID:       hr.incID(),
		Name:     name,
		Velocity: velocity,
	})
	return &dep.Employees[len(dep.Employees) - 1]
}

func (hr *HumanResources) FindDepartmentByID(id int64) *Department {
	for i := range hr.Departments {
		if hr.Departments[i].ID == id {
			return &hr.Departments[i]
		}
	}
	return nil
}

func (hr *HumanResources) EmployeesNumber() int {
	sum := 0
	for i := range hr.Departments {
		sum += len(hr.Departments[i].Employees)
	}
	return sum
}

func (hr *HumanResources) FindEmployeeByID (id int64) *Employee {
	for i := range hr.Departments {
		for j := range hr.Departments[i].Employees {
			if hr.Departments[i].Employees[j].ID == id {
				return &hr.Departments[i].Employees[j]
			}
		}
	}
	return nil
}

func removeDepartment(s []Department, i int) []Department {
    s[len(s) - 1], s[i] = s[i], s[len(s) - 1]
    return s[:len(s) - 1]
}

func removeEmployee(s []Employee, i int) []Employee {
    s[len(s) - 1], s[i] = s[i], s[len(s) - 1]
    return s[:len(s) - 1]
}

func (hr *HumanResources) RemoveByID (id int64) {
	for i := range hr.Departments {
		if hr.Departments[i].ID == id {
			hr.Departments = removeDepartment(hr.Departments, i)
			return
		}
		for j := range hr.Departments[i].Employees {
			if hr.Departments[i].Employees[j].ID == id {
			    hr.Departments[i].Employees = removeEmployee(hr.Departments[i].Employees, j)
				return
			}
		}
	}
}


