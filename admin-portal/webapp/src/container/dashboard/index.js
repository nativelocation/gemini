import React, {Component} from 'react'
import { Link, Switch, Redirect, Route, withRouter } from 'react-router-dom'
import NavDashboard from '../../components/NavDashboard'
import routes from './routes'

import userPlaceholder from '../../assets/img/userPlaceholder.png'
class Dashboard extends Component {
  render() {
      return (
        <div className='dashboard'>
          <div className='main'>
            <NavDashboard />
            <div className='sidebar'>
              <div className='userInfo flexCenter'>
                <img className='userAvatar' src={userPlaceholder} alt='' />
                <div className='userName'>John Smith</div>
              </div>
              <ul className='nav'>
                <li>
                  <Link href='#mainchart' to='/dashboard/mainchart'>
                    
                  </Link>
                </li>
              </ul>
              <Link className='logout' to='/logout' href='#/logout'>
                <span/>Logout
              </Link>
            </div>
            <div className="display">
              {/* <Switch>
                <Route
                  path="/dashboard/mainchart"
                  component={MainChart}
                />
                <Redirect from="*" to="/dashboard/mainchart" />
              </Switch> */}
            </div>
          </div>
        </div>
      )
  }
}

export default withRouter(Dashboard);
