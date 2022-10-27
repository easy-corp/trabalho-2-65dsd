# trabalho-2-65dsd
Desenvolvimento do Segundo Trabalho da Matéria de Desenvolvimento de Sistemas Paralelos e Distribuídos 
do curso de Engenharia de Software da Universidade do Estado de Santa Catarina.

Alunos: Gabriel Dolzan, Luis Felipe da Silva e Murilo Goedert.

# Instruções de Execução
Para desenvolvimento do projeto, foi utilizada a linguagem Java com a biblioteca JavaFX. Para facilitar 
todo o processo, utiliza-se o Maven para baixar todas as dependências necessárias.

Para executar o projeto selecione a opção “Run as Maven Build”, o projeto não executará se for selecionada 
a opção “Run as Java Application”, diferentes IDE´s possuem diferentes formas de selecionar essas opções. 
No parâmetro GOALS, preencha a seguinte informação: **clean javafx:run -e -X**.

# Visual Studio Code

Se você utilizar a IDE VSCode é necessário verificar se o Maven está instalado no seu computador e, caso não esteja,
fazer donwload do mesmo. Após feito, basta abrir o terminal da própria IDE e inserir o comando: **mvn clean javafx:run -e -X**

![image](https://user-images.githubusercontent.com/44239991/198272990-1d99e932-491d-4fe3-a754-8de02ae0ddf8.png)

# NetBeans

Os usuários do NetBeans tem uma vantagem nesse quesito, tendo em vista que o Maven já é instalado junto ao programa. 
Por conta disso, não é necessário fazer nenhuma configuração externa para a execução. Dentro da IDE, basta apertar com o botão direito
na classe main do projeto, ir até a opção RUN MAVEN -> GOALS e então setar o parâmetro: **clean javafx:run -e -X**.

![198273610-cb413a7a-e62e-4b49-ba7c-a93203c53d97](https://user-images.githubusercontent.com/44239991/198341862-97c38b2b-7502-45e8-99f4-1022ffa3bb80.png)
