Текущая задача
1. Текущий проект представляет собой root calculating-factorial с модулями:
    - client
    - server
    - grpc-api
    
2. Модуль client:
    - принимает по HTTP протоколу запросы:
        - **/api/converter  post запрос по модели CalculatingRequest с двумя параметрами:
            - количество потоков
            - число для вычеслений

        - **/api/converter/stop  post запрос по модели CalculatingRequest для остановки ранее запущенного вычесления:
            - из модели берет UID вычесления

2.1. Сервис обрабатывающий HTTP запросы от  пользователя (ConverterHttpGrpcService)
    - проверяет поступивший от пользователя UID вычесления
    - проверяет поступившие от пользователя параметры, должны быть строго больше 0 
            - количество потоков
            - число для вычеслений
    - обрабатывает запросы по двум сценариям:
            - запросы на создание нового вычесления (startCalculat)
            - запросы на совершение манипуляций с имеющимися вычеслениями (stopCalculat,getCalculatStatus,recommenceCalculating)

2.2.

3. Модуль server:
    - принимает по gRPC протоколу запросы


Что сделать долги:
    - протестить класс GrpcServiceEventImpl     
    - основное вычесление пока только  запускается, результат никак не используется
    - статус возвращает из текущей переменной, а должно из БД

Текущие задачи
    - протестить getEventCalculation
    - сохранеие в БД
    - думаем как вернуть итоговое значение
    - создать класс-имитацию для генерации запросов на http сервер


1.Полностью написать клиент с тестами
2.Разобраться с Grpc запросами
    - принять запрос на стороне сервера- готово
    - все покрыть тестами
    - на данный момент флаг на статус вычеслений передаем не через Boolean, а через enum TypeEvent
    - тесты на GrpcServiceImpl не написаны - ожидаем когда будет создан response от сервера
    - не выстроена работа с Json
2.1 Server
    Вариант 1 логика отправки запроса и получения результата это два разных события
        - при получение запроса старт возвращаем только UD вычесления
        - при получении запроса стоп возвращаем сообщение о результатах выполнения команды
                (не имеется, вычесление окончено, вычисление остановлено)
        - при получении запроса о статусе возращаем такое же сообщение 
                + уведомление о прекращении вычесления в связи с остановкой сервера
        - при получении запроса о возобновлении возвращаем сообщение

        - по каждому вычеслению создается запись в БД
            - храним UD, статус, число, потоки, результат на момент остановки по запросу пользователя или остановки сервера
        - по окончанию вычесления сервер сам формирует и отсылает сообщение о результатах вычесления с указанием
            + UD вычесления, потоки, число, результат


        - результат в базу сохраняется либо при остановке пользователем, либо при остановке сервера



3.Будет ли docker
        - объединить все в один докер-композ и залить на докер хаб

Разное
- БД в докере по любому делать