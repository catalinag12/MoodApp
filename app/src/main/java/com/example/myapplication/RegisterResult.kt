import com.google.firebase.auth.FirebaseUser

sealed class RegisterResult {
    data class Success(val user: FirebaseUser) : RegisterResult()
    data class Error(val errorMessage: String?) : RegisterResult()
}
