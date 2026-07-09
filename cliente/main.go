package main

import (
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
)

type Item struct {
	Name string `json:"name"`
}

func helloName(ctx *gin.Context) {
	var item Item

	if err := ctx.BindJSON(&item); err != nil {
		fmt.Print(err)
		ctx.IndentedJSON(http.StatusBadRequest, gin.H{
			"message": "Invalid request",
		})
		return
	}

	ctx.IndentedJSON(http.StatusOK, item)
}

func main() {
	// Create a Gin router with default middleware (logger and recovery)
	router := gin.Default()

	router.POST("/item", helloName)

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
