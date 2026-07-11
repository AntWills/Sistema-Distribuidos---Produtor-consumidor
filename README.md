# O Problema do Produtor-Consumidor em uma Lanchonete Online

Este repositório foi desenvolvido para a avaliação final da disciplina de Sistemas Distribuídos. Nele são implementados três sistemas (APIs) independentes, sendo eles Cliente, Lanchonete e Faturamento, isolados em containers próprios, para simular o uso de diferentes máquinas, que se comunicam exclusivamente por meio de requisições HTTP.

Integrantes do grupo
- Antonio Wills da Silva Santos
- Evelly Victory Vieira Pinto
- Francisco Leonel Ferreira dos Santos
- Jonas Davi Nogueira Sena
- Luís Felipe Assunção Pereira

## Produtor-Consumidor

O padrão Produtor-Consumidor desacopla a geração do processamento dos dados: produtores criam informações e as depositam em filas ou buffers, enquanto consumidores as processam de forma assíncrona. Essa separação garante escalabilidade e tolerância a falhas, já que cada parte processa seus dados em sistemas independentes.

## Motivação da Lanchonete Online

Uma lanchonete online foi escolhida como aplicação de exemplo por sua simplicidade, por permitir visualizar três sistemas funcionando de forma independente e por possibilitar a demonstração de dois cenários distintos do padrão.

Outro motivo foi o uso de tecnologias diferentes em cada serviço, no fim, é a forma como os sistemas se comunicam, e não como cada um é implementado internamente.

## 1º Cenário

Neste cenário, o Cliente atua como produtor, enquanto a Lanchonete e o Faturamento atuam como consumidores. Ao enviar uma requisição de criação de pedido para a API do Cliente, esse pedido é repassado aos outros dois sistemas: a Lanchonete inicia o preparo, enquanto o Faturamento registra os dados financeiros da compra. Caracteriza-se como Produtor-Consumidor porque a Lanchonete e o Faturamento dependem dos dados gerados pelo Cliente para executar seu próprio processamento.

## 2º Cenário

O segundo cenário é consequência do primeiro. Como um pedido não fica pronto instantaneamente, é necessário que a Lanchonete informe aos interessados, neste caso, o Cliente, qual é o status atual do pedido. Aqui os papéis se invertem: a Lanchonete passa a ser a produtora da informação de status, enquanto o Cliente se torna o consumidor, consultando periodicamente o andamento do seu pedido.

## Sobre a Aplicação

Todos os dados são armazenados em memória, sem uso de um banco de dados real, já que a aplicação tem finalidade exclusivamente demonstrativa do padrão Produtor-Consumidor.

O arquivo `docker-compose.yaml` orquestra todos os sistemas, e recomenda-se iniciar todos os serviços por meio dele.

### Sistema do Cliente

Construído em Go, utilizando o framework Gin.

#### Rotas

URL base: `http://localhost:8080`, referenciada aqui como `cliente-back`.

- **POST** `cliente-back`

Req:
```json
{
  "item_id": 3
}
```
Res:
```json
{
  "id": 976861,
  "status": "PREPARANDO"
}
```

- **GET** `cliente-back/order/976861`

Res:
```json
{
  "id": 976861,
  "status": "COMPLETO"
}
```

- **GET** `cliente-back/items`

Res:
```json
[
  { "id": 1, "name": "Caldo de Peixe", "price": 35 },
  { "id": 2, "name": "Salgado de Queijo", "price": 15 },
  { "id": 3, "name": "Bolo de Cenoura", "price": 10 }
]
```

### Sistema da Lanchonete

Construído em Java, utilizando o framework Spring Boot.

#### Rotas

URL base: `http://localhost:8081`, referenciada aqui como `lanchonete-back`.

- **GET** `lanchonete-back/items`

Res:
```json
[
  { "id": 1, "name": "Caldo de Peixe", "price": 35 },
  { "id": 2, "name": "Salgado de Queijo", "price": 15 },
  { "id": 3, "name": "Bolo de Cenoura", "price": 10 }
]
```

- **GET** `lanchonete-back/orders`

Res:
```json
[
  { "id": 93958, "status": "COZINHANDO" },
  { "id": 976861, "status": "COZINHANDO" },
  { "id": 976861, "status": "PREPARANDO" },
  { "id": 976861, "status": "COMPLETO" }
]
```

- **POST** `lanchonete-back`

Req:
```json
{
  "item_id": 3
}
```
Res:
```json
{
  "id": 976861,
  "status": "PREPARANDO"
}
```

- **GET** `lanchonete-back/orders/976861`

Res:
```json
{
  "id": 976861,
  "status": "COMPLETO"
}
```

- **PATCH** `lanchonete-back/orders/976861/next`

Rota responsável por avançar o status do pedido para a próxima etapa.

Res:
```json
{
  "id": 976861,
  "status": "COMPLETO"
}
```

### Sistema de Faturamento

Construído em Python, utilizando o framework FastAPI.

#### Rotas

URL base: `http://localhost:8082`, referenciada aqui como `faturamento-back`.

- **GET** `faturamento-back/revenue`

Res:
```json
{
  "revenue_all": 40.0,
  "revenue_item": {
    "2": 30.0,
    "3": 10.0
  }
}
```
> `revenue_all` é o total faturado no dia; `revenue_item` é o faturamento por item do cardápio.

- **POST** `faturamento-back/revenue`

Req:
```json
{
  "item_id": 1,
  "price": 35.0
}
```
Res:
```json
{
  "status": "sucesso",
  "mensagem": "Faturamento de R$ 35.0 registrado para o item 1."
}
```