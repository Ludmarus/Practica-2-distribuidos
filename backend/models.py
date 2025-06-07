from datetime import datetime
from zoneinfo import ZoneInfo
from flask_sqlalchemy import SQLAlchemy

# Inicialización de SQLAlchemy
db = SQLAlchemy()

class User(db.Model):
    """
    Modelo de usuario para la base de datos.
    Contiene credenciales básicas y metadatos de creación.
    """
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(30), unique=True, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.now(ZoneInfo("Europe/Madrid")))

    def __repr__(self) -> str:
        """
        Representación legible del objeto User (útil para debugging).
        """
        return f"<User {self.username}>"