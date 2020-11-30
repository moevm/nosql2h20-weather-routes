### Общие

0. Выбрать все

MATCH (a) return a

0. Выбрать по id

MATCH (s)
WHERE ID(s) = 65110
RETURN s

### Создание

1. Добавление Point

CREATE (c:Point {osm_id: 1, lat: 22, lon: 22, precipitation_value: 0})

2. Обновление Point precipitation_value

MATCH (n: Point { osm_id: 3 })
SET n.precipitation_value = 15
RETURN n

3. Добавление Way (с расстоянием)

MATCH (a:Point),(b:Point)
WHERE a.osm_id = 1 AND b.osm_id = 2
CREATE (a)-[r:WAY { distance: distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: b.lat, longitude: b.lon}))}]->(b)


4. Добавление Object

CREATE (c:Object {name:'Собор' , street: 'Невский проспект', house_number: '14/3'})

5. Добавление Member

MATCH (a:Object),(b:Point)
WHERE a.name = 'Собор' AND b.osm_id = 3
CREATE (a)-[r:MEMBER]->(b)
RETURN type(r)

### ДЛЯ ТАБЛИЦЫ

6. Вытащить всех Object

MATCH (n: Object)
RETURN n

7. Вытащить Object и все его Point

MATCH (o:Object), (p: Point)--(o)
WHERE ID(o) = 7
RETURN o, p

### ДЛЯ ФИЛЬТРАЦИИ ТАБЛИЦЫ

8. Вытащить только Object с name

MATCH (o:Object {name: 'Собор'})
RETURN o

9. Вытащить только Object с street (house_number)

MATCH (o:Object )
WHERE o.street CONTAINS 'Невский проспект'
RETURN o

10. Вытащить Object ПО АДРЕСУ (УЛИЦА + ДОМ)

MATCH (o:Object )
WHERE 'Невский проспект 14/3' CONTAINS o.street+ ' '+o.house_number
RETURN o

