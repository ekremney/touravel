from flask import request
from .. import db
from main.models.user import User
from . import api
from .errors import bad_request, unauthorized, forbidden
from main.responder.response import render_response
from main.decorators import protected_realm

@api.route('/api/v1.0/user', methods=['POST'])
def register():
	user = User.from_json(request.json)
	return render_response(201, {'message': 'User is successfully created.'})

@api.route('/api/v1.0/user', methods=['GET'])
@protected_realm
def get_users(user):
	info = user.get_info(request.headers)
	return render_response(200, info)

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

@api.route('/api/v1.0/user/change_password', methods=['POST'])
@protected_realm
def change_password(user):
	user.change_password(request.json)
	return render_response(201, {'message': 'Password is changed successfully'})

@api.route('/api/v1.0/user/search', methods=['POST'])
@protected_realm
def user_search(user):
	result = user.user_search(request.json)
	return render_response(200, result)

@api.route('/api/v1.0/user/follow', methods=['GET'])
@protected_realm
def follow(user):
	if user.follow(request.headers):
		return render_response(200, {'message': 'You followed: ' + request.headers.get('username')})

@api.route('/api/v1.0/user/unfollow', methods=['GET'])
@protected_realm
def unfollow(user):
	if user.unfollow(request.headers):
		return render_response(200, {'message': 'You unfollowed: ' + request.headers.get('username')})

@api.route('/api/v1.0/user/edit_profile', methods=['POST'])
@protected_realm
def edit_profile(user):
	user.edit_profile(request.json)
	return render_response(200, {'message': 'Profile has been edited successfully'})

@api.route('/api/v1.0/user/avatar', methods=['POST'])
@protected_realm
def upload_avatar(user):
	user.change_avatar(request.json)
	return render_response(201, {'message': 'Avatar uploaded successfully'})

@api.route('/api/v1.0/user/avatar', methods=['GET'])
@protected_realm
def fetch_avatar(user):
	avatar = user.fetch_avatar(request.headers)
	return render_response(200, {'data': avatar})

@api.route('/api/v1.0/user/route', methods=['POST'])
@protected_realm
def post_route(user):
	user.post_route(request.json)
	return render_response(201, {'message': 'Route posted successfully'})

@api.route('/api/v1.0/user/route', methods=['GET'])
@protected_realm
def fetch_route(user):
	route = user.fetch_route(request.headers)
	return render_response(200, route)

@api.route('/api/v1.0/timeline', methods=['POST'])
@protected_realm
def post_timeline(user):
	user.post_timeline(request.json)
	return render_response(201, {'message': 'Timeline message has been posted!'})

@api.route('/api/v1.0/timeline', methods=['GET'])
@protected_realm
def fetch_timeline(user):
	timeline = user.fetch_timeline()
	return render_response(200, timeline)







