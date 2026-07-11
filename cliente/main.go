package main

import (
	"bytes"
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
	var order Order

	if err := ctx.ShouldBindJSON(&order); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{
			"error": err.Error(),
		})
		return
	}

	url := os.Getenv("URL_LANCHONETE") + "/orders"

	body, err := json.Marshal(order)
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
		return
	}

	resp, err := http.Post(url, "application/json", bytes.NewBuffer(body))
	if err != nil {
		ctx.JSON(http.StatusBadGateway, gin.H{
			"error": err.Error(),
		})
		return
	}
	defer resp.Body.Close()

	var response any
	if err := json.NewDecoder(resp.Body).Decode(&response); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
		return
	}

	ctx.JSON(resp.StatusCode, response)
}

type CheckStatus struct {
	OrderId int64 `json:"order_id"`
}

func checkOrderStatus(ctx *gin.Context) {
	orderID := ctx.Param("id")

	baseURL := os.Getenv("URL_LANCHONETE")

	url := fmt.Sprintf("%s/orders/%s", baseURL, orderID)

	resp, err := http.Get(url)
	if err != nil {
		ctx.JSON(http.StatusBadGateway, gin.H{"error": "Não foi possível conectar ao serviço de lanchonete"})
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		ctx.JSON(resp.StatusCode, gin.H{"error": "Pedido não encontrado ou erro na lanchonete"})
		return
	}

	var lanchoneteResp any
	if err := json.NewDecoder(resp.Body).Decode(&lanchoneteResp); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": "Erro ao processar dados da lanchonete"})
		return
	}

	ctx.JSON(http.StatusOK, lanchoneteResp)
}
func main() {
	// Create a Gin router with default middleware (logger and recovery)
	router := gin.Default()

	router.POST("/items", listItens)
	router.POST("/order", createOrder)
	router.GET("/order/:id", checkOrderStatus)

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
