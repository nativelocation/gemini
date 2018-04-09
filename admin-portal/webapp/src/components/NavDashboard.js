import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router-dom'
import { connect } from 'react-redux'

import logo from '../assets/img/logo.png'
import userPlaceholder from '../assets/img/userPlaceholder.png'

class NavDashboard extends React.Component {
  state = {
    isVisible: false
  }

  render() {
    let style = {}
    return (
      <nav className='navbar navbar-default' style={style}>
        <div className='navbar-header'>
          <Link className='navbar-brand' to='/home'>
            <img src={logo} alt='' />
          </Link>
        </div>
        <ul className='nav navbar-nav navbar-right'>
          <li className='dropdown'>
            <a
              className='dropdown-toggle'
              onClick={e => {
                e.preventDefault()
                this.setState({ isVisible: !this.state.isVisible })
              }}
              href='#dropdown'
            >
              <img className='userlogo' src={userPlaceholder} alt='' />
              <b className='caret' />
            </a>
            <ul
              className='dropdown-menu'
              id='togglable'
              style={{
                display: this.state.isVisible === true ? 'block' : 'none'
              }}
            >
              <li>
                <Link to='/user/logout'>Logout</Link>
              </li>
            </ul>
          </li>
        </ul>
      </nav>
    )
  }
}

export default NavDashboard
