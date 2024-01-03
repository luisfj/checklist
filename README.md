<h1 align="center">
  API de Controle de Tarefas
</h1>

<p align="center">
 <img src="https://img.shields.io/badge/-LinkedIn-02569B?logo=linkedin&logoColor=white&style=fot-the-badge" alt="luis-fernando-johann" />
</p>

API que disponibiliza um controle de tarefas reativo simplificado.

## Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Webflux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [SpringDoc OpenAPI 3](https://springdoc.org/#spring-webflux-support)

## Como Executar

### VSCode (Dev Containers)

- Instalar a extensão [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
- Clonar repositório git
- Disponibilizar as credenciais git pelo ssh-agent.
    Seguir [tutorial](https://code.visualstudio.com/remote/advancedcontainers/sharing-git-credentials)
- No VSCode apertar F1 e selecionar a opção "Dev Containers: Open Workspace in Container..."
- Selecionar o arquivo "checklist.code-workspace" na raiz do projeto
- Após abrir o projeto pelo Dev Containers, abrir o arquivo "CkecklistApplication.java" e executar/debugar o mesmo

### Localmente
- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Executar:
```
java -jar target/checklist-0.0.1-SNAPSHOT.jar
```

## Acessando a API

A API poderá ser acessada em [localhost:9000](http://localhost:9000).
O Swagger poderá ser visualizado em [localhost:9000/swagger-ui.html](http://localhost:9000/swagger-ui.html)
