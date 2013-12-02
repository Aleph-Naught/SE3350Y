package se3350.aleph.Login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import se3350y.aleph.MainDataEntry.MainDataEntry;
import se3350y.aleph.firealertscanner.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	private static final String FILENAME = "UserAccounts.txt";
	private static final String ADMINKEYS = "AdminKeys.txt";
	
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	
	private static int [] userHashCodes;
	private static int [] passwordHashCodes;
	private UserLoginTask mAuthTask = null;

	// Values for username and password at the time of the login attempt.
	private String mUsername;
	private String mPassword;
	
	private boolean cancel;

	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		setTitle(R.string.title_activity_login);

		// Set up the login form.
		mUsername = getIntent().getStringExtra(EXTRA_EMAIL);
		mUsernameView = (EditText) findViewById(R.id.email);
		mUsernameView.setText(mUsername);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
				try {
					getCredentials();
				} catch (FileNotFoundException e) {
					Log.i("Login", "Can't read info from SD Card");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void getCredentials() throws IOException {
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> passwords = new ArrayList<String>();
//		Scanner input = new Scanner(userFile);
//		String raw = input.next();
//		input.close();
		FileInputStream fis = openFileInput(FILENAME);
		String raw = "";
		int content;
		while ((content = fis.read()) != -1){
			raw += (char) content;
		}
		String [] rawArray = raw.split(":");
		for (int i=0; i<rawArray.length; i+=2) {
			users.add(rawArray[i]);
			passwords.add(rawArray[i+1]);
		}
		userHashCodes = new int [users.size()];
		passwordHashCodes = new int [passwords.size()];
		for (int i=0; i<users.size(); i++) {
			userHashCodes[i] = Integer.parseInt(users.get(i));
			passwordHashCodes[i] = Integer.parseInt(passwords.get(i));
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		
		int userHash = mUsername.hashCode();
		int passHash = mPassword.hashCode();

		cancel = false;
		boolean create = true;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_short_password));
			focusView = mPasswordView;
			cancel = true;
		} 
		

		// Check for a valid username.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		} 
		if (!cancel) {
			if (userHashCodes != null){
				int userIndex = 0;
				for (int i=0; i<userHashCodes.length; i++) {
					if (userHashCodes[i]==userHash) {
						create=false;
						userIndex=i;
					}
				}
				if (!create) {
					boolean found = false;
					if (passwordHashCodes[userIndex]==passHash)
						found=true;
					if (!found) {
						mPasswordView.setError(getString(R.string.error_incorrect_password));
						focusView = mPasswordView;
						cancel=true;
					}
				}
				else {
					adminValidate(userHash,passHash);
					return;
				}
			}
			else {
				adminValidate(userHash,passHash);
				return;
			}
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			if (focusView!=null)
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
			Intent intent = new Intent(this, MainDataEntry.class);
			startActivity(intent);
		}
	}
	
	private void adminValidate(final int u, final int p) {
		try {
		FileInputStream aIn = openFileInput(ADMINKEYS);
		String raw = "";
		int content;
		while ((content = aIn.read()) != -1){
			raw += (char) content;
		}
		aIn.close();
		final String [] adminHashArray = raw.split(":");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Admin Permission Required");
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_adminkey, null);
		final EditText keyView = (EditText) dialogView.findViewById(R.id.keyField);
		builder.setView(dialogView);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which) {
			// Retrieve the data from the Dialog
			int keyHash = keyView.getText().toString().hashCode();
			boolean found = false;
			for (int i=0; i<adminHashArray.length; i++) {
				if (Integer.parseInt(adminHashArray[i])==keyHash) {
					registerUser(u,p);
					cancel = false;
					found = true;
				}
			}
			if (!found)
				Toast.makeText(getBaseContext(), getString(R.string.error_invalid_adminkey), Toast.LENGTH_SHORT).show();
		}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				cancel = true;
				
			}
		});
		builder.show();
		//return true;
		}
		catch (FileNotFoundException e) {
			Log.i("Admin Validation", "FileNotFoundException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Admin Validation", "IOException");
			e.printStackTrace();
		}
		Log.i("Admin Validation", "method called");
		//return doNotProceed;
	}
	
	private void registerUser(int user, int pass) {
		try {
			
			
			
			PrintWriter pw = new PrintWriter(openFileOutput(FILENAME, MODE_APPEND));
			String toWrite = ":"+user+":"+pass;
//			for (int i=0; i<userHashCodes.length; i++) {
//				toWrite+=":"+userHashCodes[i]+":"+passwordHashCodes[i];
//			}
			pw.write(toWrite);
			pw.close();
			Toast.makeText(getBaseContext(), getString(R.string.user_registered), Toast.LENGTH_SHORT).show();
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
			Intent intent = new Intent(this, MainDataEntry.class);
			startActivity(intent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.


			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mUsername)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
