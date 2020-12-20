### Общие

1. Выбрать все

MATCH (a) return a

2. Выбрать по id

MATCH (s)
WHERE ID(s) = 65110
RETURN s

### Дополнительные для заполнения БД

3. Выбрать Point по osm_id

MATCH (p:Point)
WHERE p.osm_id = 1
RETURN s

4. Выбрать Way по osm_id и вернуть id его точек

MATCH (a:Point), (b:Point), r = (a)-[:WAY {osm_id: 1}]->(b)
RETURN a.osm_id, b.osm_id

### Создание

5. Добавление Point

CREATE (c:Point {osm_id: 1, lat: 22, lon: 22, precipitation_value: 0})

6. Обновление Point precipitation_value

MATCH (n: Point { osm_id: 3 })
SET n.precipitation_value = 15
RETURN n

7. Добавление Way (с расстоянием)

MATCH (a:Point),(b:Point)
WHERE a.osm_id = 1 AND b.osm_id = 2
CREATE (a)-[r:WAY { osm_id: 1, distance: distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: b.lat, longitude: b.lon}))}]->(b)


8. Добавление Object

CREATE (c:Object {osm_id: 1, name:'Собор' , street: 'Невский проспект', house_number: '14/3'})

9. Добавление Member

MATCH (a:Object),(b:Point)
WHERE a.osm_id = 1 AND b.osm_id = 3
CREATE (a)-[r:MEMBER]->(b)
RETURN type(r)

### ДЛЯ ТАБЛИЦЫ

10. Вытащить всех Object

MATCH (n: Object)
RETURN n

11. Вытащить Object и все его Point

MATCH (o:Object), (p: Point)--(o)
WHERE ID(o) = 5
RETURN o as Object, collect(p) as Points

12. Вытащить ВСЕХ Object со ВСЕМИ point

MATCH (n: Object) , m = (n)-[:MEMBER]-(p: Point)
RETURN  n as Object, collect(p) as Points

### ДЛЯ ФИЛЬТРАЦИИ ТАБЛИЦЫ

13. Вытащить только Object с name

MATCH (o:Object)
WHERE o.name CONTAINS 'Собор'
RETURN o

14. Вытащить только Object с street (house_number)

MATCH (o:Object )
WHERE o.street CONTAINS 'Невский проспект'
RETURN o

15. Вытащить Object ПО АДРЕСУ (УЛИЦА + ДОМ)

MATCH (o:Object )
WHERE 'Невский проспект 14/3' CONTAINS o.street AND 'Невский проспект 14/3' CONTAINS o.house_number
RETURN o

### ДЛЯ ПОИСКА ПУТЕЙ И СТАТИСТИКИ

16. Поиск максимально близкой Point по координатам

MATCH (a: Point)
WHERE min(distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: 22.2, longitude: 22.2})))
RETURN distance

MATCH (a: Point)
WHERE distance(point({latitude: a.lat, longitude: a.lon}), point({latitude: 22, longitude: 22})) = distance
RETURN a

17. Самый короткий путь, где осадки меньше какого-то числа: **возвращает сразу протяженость пути**

MATCH (a:Point {osm_id:1} ), (b:Point {osm_id: 3}), p=allShortestPaths((a)-[*]-(b))
WHERE ALL(r IN relationships(p) WHERE endNode(r).precipitation_value < 30 AND type(r)= 'WAY')
RETURN p, reduce(totalDistance = 0, x in relationships(p)| totalDistance + x.distance) as dist
ORDER BY dist ASC 