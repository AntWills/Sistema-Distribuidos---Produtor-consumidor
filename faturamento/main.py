from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="Microserviço de Faturamento")

# Banco de dados temporário em memória
dados_faturamento = {
    "revenue_all": 0.0,
    "revenue_item": {}
}

# Modelo para validar o corpo da requisição POST
class ItemFaturamento(BaseModel):
    item_id: int
    price: float

@app.post("/revenue")
def registrar_faturamento(item: ItemFaturamento):
    # 1. Acumula no faturamento total
    dados_faturamento["revenue_all"] += item.price
    
    # 2. Acumula no faturamento específico do item
    id_str = str(item.item_id) # Transformamos em string para manter o padrão do JSON
    if id_str in dados_faturamento["revenue_item"]:
        dados_faturamento["revenue_item"][id_str] += item.price
    else:
        dados_faturamento["revenue_item"][id_str] = item.price
        
    return {
        "status": "sucesso",
        "mensagem": f"Faturamento de R$ {item.price} registrado para o item {item.item_id}."
    }

@app.get("/revenue")
def obter_faturamento():
    # Retorna os dados acumulados em formato JSON
    return dados_faturamento