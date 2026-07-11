package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

type Item struct {
	ID    int64   `json:"id"`
	Name  string  `json:"name"`
	Price float64 `json:"price"`
}

func listItens(ctx *gin.Context) {
	url_lanchonete := os.Getenv("URL_LANCHONETE")

	resp, err := http.Get(url_lanchonete + "/items")

	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
		return
	}

	defer resp.Body.Close()

	var itens []Item

	if err := json.NewDecoder(resp.Body).Decode(&itens); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
		return
	}

	ctx.IndentedJSON(http.StatusOK, itens)
}

type Order struct {
	ItemID int64 `json:"item_id"`
}

func createOrder(ctx *gin.Context) {
	url_lanchonete := os.Getenv("URL_LANCHONETE")

	fmt.Print("Carregou: " + url_lanchonete)

	ctx.IndentedJSON(http.StatusAccepted, url_lanchonete)
}

func main() {
	// Create a Gin router with default middleware (logger and recovery)
	router := gin.Default()

	router.POST("/items", listItens)
	router.POST("/order", createOrder)

	// Define a simple GET endpoint
	router.GET("/ping", func(c *gin.Context) {
		// Return JSON response
		c.JSON(http.StatusOK, gin.H{
			"message": "pong",
		})
	})

	// Start server on port 8080 (default)
	// Server will listen on 0.0.0.0:8080 (localhost:8080 on Windows)
	router.Run()
}
