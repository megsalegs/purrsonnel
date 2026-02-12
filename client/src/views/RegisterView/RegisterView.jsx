import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Notification from '../../components/Notification/Notification';
import AuthService from '../../services/AuthService';

import styles from './RegisterView.module.css';

export default function RegisterView() {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    username: '',
    name: '',
    password: '',
    confirmPassword: '',
    address: '',
    city: '',
    stateCode: '',
    zip: '',
    role: 'user',
  });

  const [notification, setNotification] = useState(null);

  function handleChange(event) {
    const { id, value } = event.target;
    setUser((prevUser) => ({
      ...prevUser,
      [id]: value,
    }));
  }

  function handleSubmit(event) {
    event.preventDefault();

    if (user.password !== user.confirmPassword) {
      setNotification({ type: 'error', message: 'Password & Confirm Password do not match' });
      return;
    }

    AuthService.register(user)
      .then((response) => {
        if (response.status === 201) {
          setNotification({ type: 'success', message: 'Registration successful! Redirecting you to login page...' });
          setTimeout(() => {
            navigate('/login');
          }, 3000);
        }
      })
      .catch((error) => {
        const response = error.response;
        if (!response) {
          setNotification({ type: 'error', message: error.message });
        } else if (response.status === 400) {
          if (response.data.errors) {
            let msg = 'Validation error: ';
            for (let err of response.data.errors) {
              msg += `'${err.field}': ${err.defaultMessage}. `;
            }
            setNotification({ type: 'error', message: msg });
          } else {
            setNotification({ type: 'error', message: `Registration failed: ${response.data.message}` });
          }
        } else {
          setNotification({ type: 'error', message: `Registration failed: ${response.data.message}` });
        }
      });
  }

  return (
    <div className={styles.page}>
      <div className={styles.content}>
    <div className={styles.container}>
      <form onSubmit={handleSubmit}>
        <h1>Create Account</h1>

        <Notification notification={notification} clearNotification={() => setNotification(null)} />

        <div className={styles.registerForm}>
          <label htmlFor="username">Username</label>
          <input
            type="text"
            id="username"
            placeholder="Username"
            value={user.username}
            onChange={handleChange}
            required
            autoFocus
          />
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            placeholder="Name"
            value={user.name}
            onChange={handleChange}
            required
          />
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            placeholder="Password"
            value={user.password}
            onChange={handleChange}
            required
          />
          <label htmlFor="confirmPassword">Confirm password</label>
          <input
            type="password"
            id="confirmPassword"
            placeholder="Confirm Password"
            value={user.confirmPassword}
            onChange={handleChange}
            required
          />
          <label htmlFor="address">Address</label>
          <input
            type="text"
            id="address"
            placeholder="Address"
            value={user.address}
            onChange={handleChange}
          />
          <label htmlFor="city">City</label>
          <input
            type="text"
            id="city"
            placeholder="City"
            value={user.city}
            onChange={handleChange}
          />
          <label htmlFor="state">State</label>
          <input
            type="text"
            id="stateCode"
            placeholder="State"
            value={user.stateCode}
            onChange={handleChange}
            maxLength="2"
            required
          />
          <label htmlFor="zip">ZIP</label>
          <input
            type="number"
            id="zip"
            placeholder="ZIP"
            value={user.zip}
            onChange={handleChange}
            required
            minLength="5"
            maxLength="5"
          />
          
          
            <button type="submit" className={styles.submitBtn}>Create Account</button>
          
        </div>
        <hr />
        <p>Have an account? <Link to="/login">Sign in!</Link></p>
      </form>
    </div>
    </div>
    <footer className={styles.footer}>
				&copy; Purrsonnel
    </footer>
  </div>
  
  );
}
