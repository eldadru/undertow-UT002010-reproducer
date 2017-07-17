package main

import (
	"net/url"
	"time"
	"fmt"
	"math/rand"
	"github.com/gorilla/websocket"
)

func main()  {
	u := url.URL{Scheme: "ws", Host: "127.0.0.1:80", Path: "/"}
	fmt.Printf("connecting to %s\n", u.String())

	ws_conn, _, err := websocket.DefaultDialer.Dial(u.String(), nil)
	if err != nil {
		fmt.Print("can't connecto to websocket server :", err)
		return
	}

	defer ws_conn.Close()

	done := make(chan struct{})

	go func() {
		defer ws_conn.Close()
		defer close(done)
		for {
			_, _, err := ws_conn.ReadMessage()
			if err != nil {
				fmt.Println("read error:", err)
				return
			}
		}
	}()

	ticker := time.NewTicker(time.Second * 1)
	defer ticker.Stop()

	data := make([]byte, 1024 * 1024)
	rand.Read(data)

	for {
		select {
		case <-ticker.C:
			err = ws_conn.WriteMessage(websocket.BinaryMessage, data)
			if err != nil {
				fmt.Println("write:", err)
				return
			}

		}
	}
}

