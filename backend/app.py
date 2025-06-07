from flask import Flask, request, jsonify
from flask_cors import CORS
from models import db, User
import bcrypt
from sqlalchemy.exc import IntegrityError

app = Flask(__name__)
CORS(app)  # Permitir peticiones CORS desde Spring u otros orígenes

# Configuración de base de datos SQLite
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite3'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)

# -----------------------------------
# Endpoint de registro de usuarios
# -----------------------------------
@app.route("/api/register", methods=["POST"])
def register():
    """
    Registra un nuevo usuario si el nombre es válido y no está en uso.
    """
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    print(f"Intento de registro: {username}")

    if User.query.filter_by(username=username).first():
        print("El usuario ya existe")
        return jsonify({"error": "Usuario ya existe"}), 400

    password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
    user = User(username=username, password_hash=password_hash.decode('utf-8'))

    db.session.add(user)
    db.session.commit()

    print(f"Usuario registrado: {username}")
    return jsonify({"message": "Usuario creado correctamente"}), 201

# -----------------------------------
# Endpoint de login
# -----------------------------------
@app.route("/api/login", methods=["POST"])
def login():
    """
    Verifica las credenciales de un usuario.
    """
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")

    user = User.query.filter_by(username=username).first()

    # 👇 COMPROBACIÓN
    print("Usuario:", username)
    print("Contraseña introducida:", password)
    print("Hash guardado:", user.password_hash if user else "Usuario no encontrado")

    if user and bcrypt.checkpw(password.encode('utf-8'), user.password_hash.encode('utf-8')):
        return jsonify({"message": "Login correcto"}), 200
    return jsonify({"error": "Credenciales incorrectas"}), 401

# -----------------------------------
# Endpoint para pruebas de errores de base de datos
# -----------------------------------
@app.route("/api/test-error", methods=["GET"])
def test_error():
    """
    Simula distintos errores en la base de datos según el parámetro ?caso=...

    Casos soportados:
      - ok: consulta normal
      - duplicado: inserta dos usuarios con el mismo nombre
      - tipo_invalido: inserta un usuario inválido (ej. sin nombre)
      - sin_contexto: fuerza un acceso fuera de contexto de aplicación
    """
    caso = request.args.get("caso", "")

    try:
        if caso == "ok":
            usuarios = User.query.all()
            return {"message": f"Usuarios encontrados: {len(usuarios)}"}

        elif caso == "duplicado":
            username = "lucia"
            user1 = User(username=username, password_hash="hash1")
            user2 = User(username=username, password_hash="hash2")
            db.session.add(user1)
            db.session.commit()
            db.session.add(user2)
            db.session.commit()
            return {"message": "Usuario duplicado insertado (no debería pasar)"}

        elif caso == "tipo_invalido":
            user = User(username=None, password_hash="hashdummy")
            db.session.add(user)
            db.session.commit()
            return {"message": "Usuario inválido insertado (no debería pasar)"}

        elif caso == "sin_contexto":
            from threading import Thread
            result = {}

            def consulta_fuera_de_contexto():
                try:
                    usuarios = User.query.all()
                    result["message"] = f"Usuarios leídos: {len(usuarios)}"
                except Exception as e:
                    result["error"] = str(e)

            t = Thread(target=consulta_fuera_de_contexto)
            t.start()
            t.join()

            if "error" in result:
                raise RuntimeError(result["error"])
            return {"message": result["message"]}

        else:
            return {"error": "Caso de prueba desconocido"}, 400

    except IntegrityError as e:
        db.session.rollback()
        mensaje = str(e.orig).lower()
        if "unique constraint" in mensaje or "unique failed" in mensaje:
            return {"error": "Duplicado: el nombre de usuario ya existe."}, 409
        elif "not null constraint" in mensaje:
            return {"error": "Columna obligatoria: falta un dato requerido."}, 409
        else:
            return {"error": f"Error de integridad: {mensaje}"}, 409

    except RuntimeError as e:
        return {"error": f"Acceso inválido al contexto de la base de datos: {str(e)}"}, 500

    except Exception as e:
        return {"error": f"Error inesperado: {str(e)}"}, 500

# -----------------------------------
# Endpoint para ver los datos del usuario
# -----------------------------------
@app.route("/api/user/<username>", methods=["GET"])
def get_user_info(username):
    """
    Devuelve la información pública del usuario.

    Incluye:
        - username: nombre de usuario
        - created_at: fecha de creación (formateada)
    """
    user = User.query.filter_by(username=username).first()

    if not user:
        return jsonify({"error": "Usuario no encontrado"}), 404

    return jsonify({
        "username": user.username,
        "created_at": user.created_at.strftime("%Y-%m-%d %H:%M:%S")  # 🆕
    })

# -----------------------------------
# Inicio de la aplicación y usuario de prueba
# -----------------------------------
if __name__ == "__main__":
    with app.app_context():
        db.create_all()

        # Usuario de prueba solo en desarrollo
        if not User.query.filter_by(username="lucia").first():
            password = "lucia"
            password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())
            test_user = User(username="lucia", password_hash=password_hash.decode('utf-8'))
            db.session.add(test_user)
            db.session.commit()
            print("Usuario de prueba creado: lucia / lucia")

    app.run(debug=True, host='0.0.0.0', port=5000)