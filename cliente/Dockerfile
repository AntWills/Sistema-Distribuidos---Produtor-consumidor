FROM golang:1.25.11-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY *.go ./
RUN CGO_ENABLED=0 GOOS=linux go build -o /docker-gs-ping

FROM alpine:3.19 


RUN apk --no-cache add ca-certificates

WORKDIR /

# A mágica acontece aqui: copiamos APENAS o binário do estágio "builder"
COPY --from=builder /docker-gs-ping /docker-gs-ping

EXPOSE 8080

CMD ["/docker-gs-ping"]