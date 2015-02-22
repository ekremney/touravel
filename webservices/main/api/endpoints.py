from flask import request
from .. import db
from main.models.user import User
from . import api
from errors import bad_request, unauthorized, forbidden
from main.responder.response import render_response
from main.decorators import protected_realm

@api.route('/api/v1.0/user', methods=['POST'])
def register():
	user = User.from_json(request.json)
	return render_response(201, {'message': 'User is successfully created.'})

@api.route('/api/v1.0/user', methods=['GET'])
def get_users():
	return render_response(200, request.headers)

@api.route('/api/v1.0/login', methods=['GET'])
def login():
	token = User.login(request.headers)
	if token is not None:
		return render_response(200, {'auth-key': token})
	return render_response(401, {'message': 'Login unsuccessful.'})

@api.route('/api/v1.0/user/change_email', methods=['POST'])
@protected_realm
def change_email(user):
	user.change_email(request.json)
	return render_response(201, {'message': 'Email is changed successfully'})

