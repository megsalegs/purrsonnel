import { NavLink } from 'react-router-dom';
import { useContext } from 'react';
import { UserContext } from '../../context/UserContext';
import styles from './NavBar.module.css';

export default function NavBar() {
  const { user } = useContext(UserContext);
  const isLoggedIn = !!user?.token;
  const isStaff = user?.role === 'ROLE_STAFF';

  return (
    <nav className={styles.nav}>
      <div className={styles.navLinks}>
      <NavLink to="/">Home</NavLink>
      <NavLink to="/cats">Cats</NavLink>
      <NavLink to="/cats/featured">Featured</NavLink>
      {isLoggedIn && <NavLink to="/hire-requests/mine">My Hires</NavLink>}
      {isStaff && <NavLink to="/hire-requests">Hire Requests</NavLink>}
      {isStaff && <NavLink to="/admin/reviews"> Manage Reviews</NavLink>}
      {isLoggedIn && <NavLink to="/bookmarks">Bookmarks</NavLink>}
      </div>
      {!isLoggedIn && <NavLink to="/login">Login</NavLink>}
      {isLoggedIn && <NavLink to="/logout">Logout</NavLink>}
      
    </nav>
  );
}
