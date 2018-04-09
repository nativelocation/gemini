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
        this.inputHandler = this.inputHandler.bind(this);
        this.validForm = this.validForm.bind(this);
        this.verifyCallback = this.verifyCallback.bind(this);
    }

    componentWillMount() {
        // this.props.cleanRegistration();
    }

    // getValidationMessages() {
    //     let messages = [];
    //     let form = this.props.form;
    //     let emailAreEquals = form.email === form.confirmEmail;

    //     if (!this.refs.email.valid())
    //         messages.push(UIHelper.getText("emailInvalid"));
    //     if (!this.refs.confirmEmail.valid())
    //         messages.push(UIHelper.getText("confirmEmailInvalid"));
    //     if (!emailAreEquals)
    //         messages.push(UIHelper.getText("emailAndConfirmInvalid"));
    //     // if (!this.state.token)
    //     //     messages.push(UIHelper.getText("missingReCaptchaToken"));
    //     return messages;
    // }

    register(e) {
        let user = this.props.form;
        e.preventDefault();
        let element = e.target.value;
        console.log(element, this.props);
        this.props.history.push('/')

        // this.props.loginUser(form)
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

    inputHandler(e) {
        let form = this.props.form;
        let element = e.target;
        console.log(element);
        form[element.id] = element.value;
        // this.validForm();
        console.log(form)
    }

    render() {
        let form = this.props.form;
        return (
            <div className='dashboard'>
                <div className='main'>
                    <nav className='navbar navbar-default'>
                        <div className='navbar-header'>
                        <Link className='navbar-brand' to='/home'>
                            <img src={logo} alt='' />
                        </Link>
                        </div>
                    </nav>
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
                                            <form onSubmit={this.register}>
                                                <div className="row plr15 ">
                                                    <div className="col-md-12">
                                                        <TextInput id="email"
                                                                type="email"
                                                                ref="email"
                                                                label="Usuario SIE"
                                                                onChange={this.inputHandler}
                                                                value={form.email}
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
                                                                onChange={this.inputHandler}
                                                                value={form.password}
                                                                iconname="icon-eye"
                                                                grouped/>
                                                    </div>
                                                </div>

                                                <div className="row mt50">
                                                    <div className="col-md-12 ">
                                                        <button
                                                            style={{paddingLeft: 20}}
                                                            className="button-green mr30 mob-mb30px"
                                                            type="submit"
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
    return { form: store.loginStore.form }
}

function mapDispatchToActions(dispatch) {
    return {
        dispatch,
        loginUser: bindActionCreators(Actions.loginUser, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToActions)(Login)
