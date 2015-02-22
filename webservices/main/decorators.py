from flask import request
from main.api.errors import unauthorized
from main.models.user import User

def protected_realm(func):
    def wrapper():
        if request.headers.get('auth-key') is not None:
            user = User.verify_auth_token(request.headers.get('auth-key'))
            if user is not None:
                return func(user)
        return unauthorized('Authentication required.')
    return wrapper