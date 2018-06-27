package com.example.totoroto.mureok.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.totoroto.mureok.Main.MainActivity
import com.example.totoroto.mureok.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var callbackManager: CallbackManager
    private lateinit var btnFacebookLogin: LoginButton
    private lateinit var btnGoogleJoin: SignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var apiClient: GoogleApiClient
    private var authListener: FirebaseAuth.AuthStateListener ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        aboutGoogleLogin()
        aboutFacebookLogin()

        currentUserState()
    }

    private fun aboutFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        btnFacebookLogin.setReadPermissions("email", "public_profile")
        btnFacebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun aboutGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        apiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

    }

    private fun currentUserState() {//로그인 상태 변화에 응답->현재어떤사용자가로그인중인지
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnGoogleJoin -> signIn()
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        //facebooklogin
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount ?: return
                firebaseAuthWithGoogle(account)
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }

                }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess)

        if (result.isSuccess) {
            val acct = result.signInAccount
        }
    }

    override fun onStop() {
        super.onStop()
        val listener = authListener ?: return

        auth.removeAuthStateListener(listener)

    }

    override fun onStart() {
        super.onStart()

        val opr = Auth.GoogleSignInApi.silentSignIn(apiClient)
        if (opr.isDone) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in")

            val result = opr.get()
            handleSignInResult(result)
        } else {
            opr.setResultCallback { googleSignInResult -> handleSignInResult(googleSignInResult) }
        }

        val listener = authListener ?: return
        auth.addAuthStateListener(listener)
    }

    private fun init() {

        btnGoogleJoin = findViewById<View>(R.id.btnGoogleJoin) as SignInButton
        btnFacebookLogin = findViewById<View>(R.id.btnFacebookJoin) as LoginButton
        auth = FirebaseAuth.getInstance()

        btnGoogleJoin.setSize(SignInButton.SIZE_STANDARD)
        btnGoogleJoin.setOnClickListener(this)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed")
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "SOLBIN"
    }
}
