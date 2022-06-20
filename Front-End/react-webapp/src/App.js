import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Route,Routes, Link} from 'react-router-dom';

import LoginComp from './components/loginComponent';
import BankingPage from './components/bankingPageComponent';

function App() {
  return (
    <Router>
      <div className="App">
        {/* <LoginComp /> */}

        <Routes>
          <Route path="/" element={ LoginComp() } />
        </Routes>

        <Routes>
          <Route path="/login" element={ LoginComp() } />
        </Routes>

        <Routes>
          <Route path="/homePage" element={ BankingPage() } />
        </Routes>

        

        
      </div>
    </Router>
  );
}

export default App;
