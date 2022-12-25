# Birthdaybot
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
## Описание
Birthdaybot - телеграмм бот, автоматизирующий задачу сбора поздравлений коллегам и друзьям на день рождения. 
Нужно лишь подклюиться к боту, представиться и добавиться в интересующие группы для начала работы. В каждой группе есть отвественны за сбор средств. Именно ему все скидывается, а он уже самостоятельно отправляет средства тому у кого ДР. 
## Команды
* `/start` -начало работы с ботом

* `/introduce name birthday` - сохранение сведений о пользователе. 
    * `name` отображаемое пользвателям имя
    * `birthday` дата рождения
* `/createteam name credentinals` - создание группы в рамках которой будет проходить рассылка
  * `name` - название группы
  * `credentinals` - номер телефона отвественного в группе. кому будут отправлться средства на поздравлени.
* `/jointeam name` - присоединиться к группе
  * `name` - Название группы
* `/deleteteam name` - удалить группу и исключить из неё всех участников
  * `name` - Название удаляемой группы
* `/leave name` - покинуть группы
  * `name` - Название покидаемой группы

## Архитектура
TODO: Добавить диагрмму классов или схему архитектуры
## Пример
Реализацию бота можно найтип о ссылке [@Birthdaymoney123bot](https://t.me/Birthdaymoney123bot)
