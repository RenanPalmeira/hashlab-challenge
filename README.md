# hashlab-challenge

[![Build Status](https://travis-ci.org/RenanPalmeira/hashlab-challenge.svg?branch=master)](https://travis-ci.org/RenanPalmeira/hashlab-challenge)

Implementação do [teste de back-end](https://github.com/hashlab/hiring) da [hashlab](https://www.hash.com.br/), o teste consiste em escrever 2 serviços (serviço de desconto, serviço de produto) e fazer uma comunicação tolerante a falhas entre eles, utilizando [gRPC](https://grpc.io/).

# Stack

O projeto está dividida em 3 (três) serviços `user-service`, `discount-service` e `product-service` utilizando os banco de dados e linguagens mais próximas da stack da hash.

- Linguagens: Clojure e Go
- Bancos de dados: PostgreSQL e MongoDB

## Serviços

- `user-service` é escrito em Go e usa o MongoDB como banco de dados
- `discount-service` é escrito em Go e faz chamadas ao `user-service`
- `product-service` é escrito em Clojure, usa o PostgreSQL como banco de dados e faz chamadas ao `discount-service`

# Distribuindo a pasta `proto`

Rode em seu terminal o comando `sh scripts/distribute_proto.sh`, esse script vai copiar/colar a pasta `proto` nos serviços.

# Executando

`docker-compose up` inicia todos os serviços e os banco de dados (inserindo alguns produtos e usuários).

## Usuários disponíveis 

Em `scripts/mongo/users.json` temos 29 usuários (com `birth_date` em UTC e com dias do mês de agosto de 2019) para testar o envio do header X-USER-ID.

# Endpoints

`GET /product` - Listagem de todos os produtos fazendo chamadas ao `discount-service` para cada produto quando for enviado o header X-USER-ID, por padrão a paginação está em 20 itens por página

Exemplo
```
{
    "data": [
        {
            "id": "897d4615-0e56-4871-bb7e-2048a4f369c9",
            "price_in_cents": 10000,
            "title": "um livro legal",
            "description": "testad",
            "discount": {
                "pct": 10,
                "value_in_cents": 1000
            }
        },
        {
            "id": "d3406a60-438b-4c73-a88b-df1b0e63d64a",
            "price_in_cents": 1,
            "title": "Thurman Bogan",
            "description": "Vero occaecati officia qui eveniet hic et tempora. Similique cupiditate dolorem omnis qui blanditiis quos voluptas delectus. Accusantium praesentium reiciendis excepturi doloremque ea tenetur. Dolor officiis quia unde labore nihil. Praesentium ad non sunt rerum quas eum amet.",
            "discount": {
                "pct": 10,
                "value_in_cents": 1000
            }
        },
        ...
    ],
    "links": {
        "self": {
            "number": 1,
            "href": "/product?page=1"
        },
        "last": {
            "number": 21,
            "href": "/product?page=21"
        },
        "first": {
            "number": 1,
            "href": "/product?page=1"
        },
        "next": {
            "number": 2,
            "href": "/product?page=2"
        }
    }
}
```

`GET /product/{product-id}` - Pegando um produto por id junto fazendo uma chamada ao `discount-service` quando for enviado o header X-USER-ID

Exemplo
```
{
    "id": "897d4615-0e56-4871-bb7e-2048a4f369c9",
    "price_in_cents": 10000,
    "title": "um livro legal",
    "description": "testad",
    "discount": {
        "pct": 10,
        "value_in_cents": 1000
    }
}
```

## Tolerância a falhas

Caso o `discount-service` ou `user-service` parar de executar/retornar algum erro, o `product-service` continua listando os produtos, a diferença é que o sub-resource de `discount` vai retornar como nulo

Exemplo
```
{
    "data": [
        {
            "id": "d3406a60-438b-4c73-a88b-df1b0e63d64a",
            "price_in_cents": 1000,
            "title": "Thurman Bogan",
            "description": "Vero occaecati officia qui eveniet hic et tempora. Similique cupiditate dolorem omnis qui blanditiis quos voluptas delectus. Accusantium praesentium reiciendis excepturi doloremque ea tenetur. Dolor officiis quia unde labore nihil. Praesentium ad non sunt rerum quas eum amet.",
            "discount": null
        },
        {
            "id": "83479ffc-8ee1-4850-aff0-26a25d134bc0",
            "price_in_cents": 190000,
            "title": "Nelson Kassulke",
            "description": "Praesentium rerum nihil dicta reiciendis cumque et architecto. Quia illo quia qui voluptate. Voluptatem beatae rerum ratione nam. Consequatur qui accusantium exercitationem recusandae vitae et reiciendis.",
            "discount": null
        },
        ...
    ],
    "links": {
        "self": {
            "number": 1,
            "href": "/product?page=1"
        },
        "last": {
            "number": 11,
            "href": "/product?page=11"
        },
        "first": {
            "number": 1,
            "href": "/product?page=1"
        },
        "next": {
            "number": 2,
            "href": "/product?page=2"
        }
    }
}
```

## Erros

Quando não é encontrado nenhum produto ou o id requisitado não foi encontrado, vai ser retornado um erro similar a esse:

```
{
    "errors": [
        {
            "type": "ResourcesNotFoundError",
            "message": "Products not found"
        }
    ],
    "url": "/product?page=42"
}
```

# Variáveis de ambiente disponíveis

- `HASHLAB_PRODUCTION_SERVICE_PER_PAGE` Configura a quantidade de produtos por página, padrão: `20` 
- `HASHLAB_DISCOUNT_SERVICE_URI` endereço do `discount-service`, padrão: `localhost:50051`
- `HASHLAB_DISCOUNT_SERVICE_PORT` porta do `discount-service` padrão: `:50051`
- `HASHLAB_USER_SERVICE_URI` endereço do `user-service` padrão: `localhost:50052`
- `HASHLAB_USER_SERVICE_PORT` porta do `user-service` padrão: `:50052`
- `HASHLAB_POSTGRES_CONNECTION_URI` URL JDBC para acessar o PostgreSQL padrão: `jdbc:postgresql://localhost:5432/hashlab?user=hashlab&password=hashlab`
- `HASHLAB_MONGODB_HOST` endereço do MongoDB padrão: `localhost:27017`
- `HASHLAB_MONGODB_USERNAME` nome de usuário do MongoDB padrão: `hashlab`
- `HASHLAB_MONGODB_PASSWORD` senha do MongoDB padrão: `hashlab`
- `HASHLAB_MONGODB_DATABASE` banco de dados do MongoDB padrão: `hashlab`
- `HASHLAB_MONGODB_AUTH_SOURCE` banco de dados de autorização do MongoDB padrão: `admin`

# Bibliotecas

## Clojure

- `pedestal`  - web framework
- `honeysql` - uma camada para converter mapas Clojure em SQL
- `lein-protoc` - plugin para implementar arquivos `.proto` para Clojure
- `environ` - acessar variáveis de ambiente

## Go

- `go.mongodb.org/mongo-driver/mongo` - driver oficial de MongoDB para a linguagem Go
- `github.com/golang/protobuf/protoc-gen-go` - plugin para implementar arquivos `.proto` para Go
- `github.com/crgimenes/goconfig` - acessar variáveis de ambiente

# Estrutura

## user-service

```
user-service
├── database
│   └── database.go // configura a conexão com o MongoDB
├── main.go // implementa o serviço gRPC e inicializa o servidor gRPC
├── model
│   └── User.go // modelo para ter acesso aos dados do MongoDB
```

## discount-service

```
discount-service
├── logic
│   ├── logic.go // regras de negócio para conceder ou não desconto 
│   └── logic_test.go // teste das regras de negócio
├── main.go // implementa o serviço gRPC e inicializa o servidor gRPC
└── util
    └── util.go // utilitários para lidar com dados e a comunicação entre o user-service
```

## product-service

```
product-service
├── src
│   └── com
│       └── hash
│           └── product
│               ├── client
│               │   └── discount.clj // interface com o discount-service
│               ├── config.clj // configurações
│               ├── controller.clj // responsável por chamar o banco de dados e discount-service para executar as regras de negócio 
│               ├── db.clj // interface com o banco de dados PostgreSQL
│               ├── interceptors
│               │   └── components.clj // utilitários para criar conexões com banco de dados e discount-service
│               ├── logic.clj // regras de negócio para lidar com a resposta do discount-service e calcular ou não o valor do desconto 
│               ├── server.clj // inicializa o servidor HTTP 
│               ├── service.clj // rotas e funções para lidar com o HTTP (headers, querystring, path)
│               └── util.clj // utilitários para lidar com configurações, gRPC, banco de dados, paginação, valores em centavos e as respostas em JSON  
└── test
    └── com
        └── hash
            └── product
                ├── controller_test.clj ;; testando as regras do controller
                └── logic_test.clj ;; testando as regras de negócio
```

# Deploy

O deploy foi feito utlizando o [Google Cloud Platform](https://cloud.google.com/) com Docker e [CoreOS](https://coreos.com/) para se aproximar ao máximo da stack da Hash.

# Links

* https://blog.jmibanez.com/2018/07/22/grpc-with-clojure-and-leiningen.html
* https://github.com/vrih/clojure-grpc-example