# Clima Agora
A hierarquia visual usa aplicativos meteorológicos como referência: cidade e temperatura em destaque, seguidas de indicadores secundários. Referência de organização:
https://support.apple.com/fr-ca/guide/weather-mac/apdw93f0ea3e/mac

A interface Swing oferece busca por cidade, consultas recentes da sessão, estados de consulta/erro e exibição das condições atuais. As consultas ocorrem fora da thread gráfica. Os símbolos meteorológicos são desenhados em Java2D; os números e textos vêm da WeatherAPI.

A captura docs/images/desktop.png mostra uma consulta real. Ela representa o horário da atualização informado na própria imagem. Não há previsão horária ou de próximos dias nesta versão.
