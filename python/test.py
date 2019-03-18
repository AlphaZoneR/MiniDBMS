import socket
import json

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(("localhost", 8990))

# s.send(json.dumps({
#     'instruction': 'create database',
#     'name': 'temporary'
# }).encode())

# s.send(json.dumps({
#     "instruction": "create table",
#     "database": "temporary",
#     "name": "foo",
#     "fields" : [
#         {
#             "name": "fooid",
#             "type": "int"
#         }
#     ]
# }).encode())

s.send(json.dumps({
    'instruction': 'get table',
    'database': 'temporary',
    'name': 'foo'
}).encode())

# s.send(json.dumps({
#     'instruction': 'alter table',
#     'database': 'temporary',
#     'name': 'foo',
#     'fields': [
#         {
#             'name': 'fooid',
#             'type': 'string',
#             'isPrimary': True
#         }
#     ]
# }).encode())

# s.send(json.dumps({
#     'instruction': 'get dropdown'
# }).encode())

print(s.recv(1024).decode())