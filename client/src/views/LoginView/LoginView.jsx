import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import { UserContext } from '../../context/UserContext';
import axios from 'axios';
import styles from './LoginView.module.css';
import { Link } from 'react-router-dom';

export default function LoginView() {
  const navigate = useNavigate();
  const { setUser } = useContext(UserContext);

  const [form, setForm] = useState({ username: '', password: '' });
  const [errorMsg, setErrorMsg] = useState('');

  function handleChange(e) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    setErrorMsg('');

    AuthService.login(form)
      .then((res) => {
        const { token, user } = res.data;

        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));

        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        setUser({ ...user, token });

        navigate('/cats');
      })
      .catch((err) => {
        setErrorMsg(err?.response?.data?.message || 'Login failed.');
      });
  }

  return (
    <div className={styles.page}>
      <div className={styles.content}>
    <div className={styles.container}>
      <form onSubmit={handleSubmit}>
        <h1>Login</h1>

        {errorMsg && <p className={styles.error}>{errorMsg}</p>}

        <div className={styles.loginForm}>
          <label htmlFor="username">Username</label>
          <input
            type="text"
            id="username"
            name="username"
            placeholder="Enter username"
            value={form.username}
            onChange={handleChange}
            required
            autoFocus
          />

          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Enter password"
            value={form.password}
            onChange={handleChange}
            required
          />

          <div></div>
          <button type="submit">Login</button>
        </div>

        <hr />

        <p>
          Don't have an account? <Link to="/register">Create one!</Link>
        </p>
      </form>
    </div>
    </div>
    <footer className={styles.footer}>
				&copy; Purrsonnel
    </footer>
  </div>
  );
}
