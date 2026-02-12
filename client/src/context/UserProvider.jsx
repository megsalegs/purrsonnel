import { useState } from 'react';
import { UserContext } from './UserContext';
import axios from 'axios';

export default function UserProvider({ children }) {
  const [user, setUser] = useState(() => getUserAndTokenFromStorage());

  // When a user comes back to the app or refreshes the page, check for user & token in local storage
  function getUserAndTokenFromStorage() {

    // Retrieve items from local storage
    const user = JSON.parse(localStorage.getItem('user')); // convert user back to object
    const token = localStorage.getItem('token');

    // User and token exists in local storage
    if (user && token) {
      // Set the token in the Axios default headers
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      // Set the user to the user state
      return { ...user, token};
    }

    // If no user/token in local storage, return null to ensure user state is assigned null
    return null;
  }

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
}
