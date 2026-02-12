import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import App from './App.jsx';
axios.defaults.baseURL = import.meta.env.VITE_REMOTE_API;
/* import fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCartPlus, faGrip, faMagnifyingGlass, faRotate, faTable, faTrashCan, faXmark, faCrown, faPalette, faWater, faStar, faPaw, faCat, faTrophy, faShieldCat, faCamera } from '@fortawesome/free-solid-svg-icons';
import { faBookmark, faCalendar } from '@fortawesome/free-regular-svg-icons';
import { BrowserRouter } from 'react-router-dom';
import UserProvider from './context/UserProvider';

/* add icons to the library */
library.add(faCartPlus);
library.add(faGrip);
library.add(faMagnifyingGlass);
library.add(faRotate);
library.add(faTable);
library.add(faTrashCan);
library.add(faXmark);
library.add(faCrown);
library.add(faPalette);
library.add(faWater);
library.add(faStar);
library.add(faBookmark);
library.add(faPaw);
library.add(faCat);
library.add(faCalendar);
library.add(faTrophy);
library.add(faShieldCat);
library.add(faCamera);




createRoot(document.getElementById('root')).render(
  <StrictMode>
    <UserProvider>
      <BrowserRouter>
        <App /> 
      </BrowserRouter>
    </UserProvider>
  </StrictMode>,
);
