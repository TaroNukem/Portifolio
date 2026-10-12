# Deploy do portfólio Spring Boot no Render

1. Copie `Dockerfile` e `.dockerignore` para a raiz do projeto, ao lado de `pom.xml`.
2. Faça commit e push desses arquivos para o GitHub.
3. No Render, crie um **Web Service** conectado ao repositório.
4. Em **Language**, selecione **Docker**.
5. Mantenha o caminho do Dockerfile como `./Dockerfile` e faça o deploy.

O Dockerfile usa Java 25 e Maven. A aplicação escuta na porta fornecida pelo Render (`PORT`).

## Variáveis de ambiente

O `.env` local não é copiado para a imagem. Se o portfólio usa tokens de API (por exemplo, GitHub ou WakaTime), cadastre no Render, em **Environment**, as mesmas variáveis que o código espera. Não coloque tokens diretamente no Dockerfile nem envie `.env` ao GitHub.

Se o build falhar, confira se a versão Java no `pom.xml` está configurada para 25 e se o projeto gera um JAR executável do Spring Boot.
