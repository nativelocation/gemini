/**
 * Created by fran on 2/1/18.
 */
import React, { Component } from "react";
import TextInput from "../../components/TextInput";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import * as UIHelper from "../../UIHelper";
import { Link } from 'react-router-dom'

import * as Actions from '../../redux/actions'
import logo from '../../assets/img/logo.png'

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            valid: false,
            token: null,
            email: '',
            password: ''
        };
        this.register = this.register.bind(this);
        this.inputHandlerEmail = this.inputHandlerEmail.bind(this);
        this.inputHandlerPass = this.inputHandlerPass.bind(this);
        this.validForm = this.validForm.bind(this);
        this.verifyCallback = this.verifyCallback.bind(this);
    }

    register(e) {
        e.preventDefault();
        this.props.loginUser({
            email: this.state.email,
            password: this.state.password
        })

    }

    verifyCallback(response) {
        let form = this.props.form;
        form.token = response;
        this.setState({...this.state, token: response}, () => {
            this.validForm();
        });

    }

    validForm() {
        let allValid = true;
        let form = this.props.form;
        let emailAreEquals = form.email === form.confirmEmail;
        let fields = this.refs;
        for (let idx in fields) {
            if (idx !== "modal")
                allValid &= fields[idx].valid();
        }
        this.setState({...this.state, valid: allValid && emailAreEquals && this.state.token})
    }

    inputHandlerEmail(e) {
        // let form = this.props.form;
        let element = e.target;
        this.setState({
            email: e.target.value
        })
        // console.log(element);
        // form[element.id] = element.value;
        // this.validForm();
        // console.log(form)
    }

    inputHandlerPass(e) {
        let element = e.target;
        this.setState({
            password: e.target.value
        })
    }

    render() {
        let form = this.props.form;
        return (
            <div className='dashboard'>
                <div className='main'>
                    {/* <nav className='navbar navbar-default'>
                        <div className='navbar-header'>
                        <Link className='navbar-brand' to='/home'>
                            <img src={logo} alt='' />
                        </Link>
                        </div>
                    </nav> */}
                    <div className='display full'>
                        <div className='panel panel-default panel-small'>
                            <div className='panel-heading'>
                                <h2>Login</h2>
                            </div>
                            <form
                                onSubmit={this.handleSubmit}
                                className='form login-form'
                            >
                                <div className='panel-body'>
                                    <div className="col-md-12 content-section">
                                        <div className="title">
                                            <div className="description"><h2>{UIHelper.getText("projectName")}</h2>
                                                <div className="violet-line"/>
                                            </div>
                                            <span className="f20slg">{UIHelper.getText("loginPage")}</span>
                                        </div>
                                        <div className="body d-flex align-items-center flex-column justify-content-end">
                                            <form>
                                                <div className="row plr15 ">
                                                    <div className="col-md-12">
                                                        <TextInput id="email"
                                                                type="email"
                                                                ref="email"
                                                                label="Usuario SIE"
                                                                onChange={this.inputHandlerEmail}
                                                                value={this.state.email}
                                                                iconname="icon-mail"
                                                                grouped/>
                                                    </div>
                                                </div>
                                                <div className="row plr15">
                                                    <div className="col-md-12">
                                                        <TextInput id="password"
                                                                type="password"
                                                                ref="password"
                                                                label="Password"
                                                                onChange={this.inputHandlerPass}
                                                                value={this.state.password}
                                                                iconname="icon-eye"
                                                                grouped/>
                                                    </div>
                                                </div>

                                                <div className="row mt50">
                                                    <div className="col-md-12 ">
                                                        <button
                                                            style={{paddingLeft: 20}}
                                                            className="button-green mr30 mob-mb30px"
                                                            // type="submit"
                                                            onClick={this.register}
                                                        >
                                                            {UIHelper.getText("loginButton")}
                                                        </button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}

function mapStateToProps(store) {
    return { form: store.login.form }
}

function mapDispatchToActions(dispatch) {
    return {
        loginUser: function(data) {
            return dispatch(Actions.loginUser(data))
        }
    }
}

export default connect(mapStateToProps, mapDispatchToActions)(Login)
