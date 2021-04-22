package main

import (
	"bytes"
	"fmt"
	terminal "github.com/wayneashleyberry/terminal-dimensions"
	"math/rand"
	"time"
)

type Field struct {
	s [][]bool
	wigth int
	height int
}

func NewField(w, h int) *Field {
	s := make([][]bool, h)
	for i := range s {
		s[i] = make([]bool, w)
	}
	return &Field{s: s, wigth: w, height: h}
}

func (f *Field) Set(x, y int, b bool) {
	f.s[y][x] = b
}

func (f *Field) Alive(x, y int) bool {
	if x < 0 || x >= f.wigth || y < 0 || y >= f.height {
		return false
	}
	return f.s[y][x]
}

func (f *Field) Next(x, y int) bool {
	// Count the adjacent cells that are alive.
	alive := 0
	for i := -1; i <= 1; i++ {
		for j := -1; j <= 1; j++ {
			if j == 0 && i == 0 {
				continue
			}
			if f.Alive(x + i, y + j) {
				alive++
			}
		}
	}
	// Return next state according to the game rules:
	//   exactly 3 neighbors: on,
	//   exactly 2 neighbors: maintain current state,
	//   otherwise: off.
	return alive == 3 || alive == 2 && f.Alive(x, y)
}

type Life struct {
	curr, next *Field
}

func NewLife(w, h int) *Life {
	a := NewField(w, h)
	for i := 0; i < (w * h / 4); i++ {
		a.Set(rand.Intn(w), rand.Intn(h), true)
	}
	return &Life{
		curr: a, next: NewField(w, h),
	}
}

func (l *Life) Step() {
	for y := 0; y < l.next.height; y++ {
		for x := 0; x < l.next.wigth; x++ {
			l.next.Set(x, y, l.curr.Next(x, y))
		}
	}
	l.curr, l.next = l.next, l.curr
}

// String returns the game board as curr string.
func (l *Life) String() string {
	var buf bytes.Buffer
	for y := 0; y < l.next.height; y++ {
		for x := 0; x < l.next.wigth; x++ {
			b := rune(' ')
			if l.curr.Alive(x, y) {
				b = '🟢'
			}
			buf.WriteRune(b)
		}
		buf.WriteByte('\n')
	}
	return buf.String()
}

func main() {
    rand.Seed(time.Now().UTC().UnixNano())
	l := NewLife(20, 10)
	width, errw := terminal.Width()
	heigth, errh := terminal.Height()
	if errw == nil && errh == nil {
		l = NewLife(int(width), int(heigth) / 2 - 2)
	}
	for {
		l.Step()
		fmt.Print("\033c", l) // Clear screen and print field.
		time.Sleep(time.Millisecond * 400)
	}
}