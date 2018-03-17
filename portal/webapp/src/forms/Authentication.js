/**
 * Created by fran on 1/24/18.
 */
import React, {Component} from "react";
import {Alert, Button, Label} from "react-bootstrap";
import "./Authentication.css";
import Immutable from "immutable";
import logo from "./logo.svg";
import {Link} from "react-router-dom";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {clean, cleanLogin, login, toggleCleanTimeout} from "../redux/actions";
import TextInput from "../components/TextInput";

class Authentication extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showAlert: false
        };

        this.login = this.login.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleDismiss = this.handleDismiss.bind(this);

    }

    componentWillMount() {
        this.props.clean();
    }

    componentWillReceiveProps(nextProps) {
        console.log(`nextProps = ${JSON.stringify(nextProps)}`);
        if (nextProps)
            this.setState({showAlert: nextProps.errorAtLogin || nextProps.invalidCredentials}, () => {
                if (nextProps.errorAtLogin || nextProps.invalidCredentials) {
                    this.props.cleanLogin();
                }

            });
    }

    handleDismiss() {
        this.props.cleanLogin()
    }

    handleInputChange(e) {
        let form = this.props.form;
        let element = e.target;
        form[element.id] = element.value;
    }

    render() {
        let form = this.props.form;
        let username = form.username;
        let password = form.password;
        let invalidCredentials = this.props.invalidCredentials;

        let showAlert = this.state.showAlert
            ? (<Alert bsStyle="danger" onDismiss={this.handleDismiss} className="auth-alert">
                <h4>Error!</h4>
                <p>
                    {invalidCredentials ? "Crendenciales invalidos" : "Ha occurido un error"}
                </p>
            </Alert>)
            : (null);
        return (
            <form className="login-block" onSubmit={this.login}>
                {showAlert}
                <img src={logo} className="App-logo" alt="logo"/>
                <h1 className="title"><Label bsStyle="primary">Registro en Linea</Label></h1>
                <TextInput id="username" includeLabel={false} placeholder="Email" required={false}
                           onChange={this.handleInputChange} value={username}/>
                <TextInput id="password" includeLabel={false} type="password" placeholder="Contraseña" required={false}
                           onChange={this.handleInputChange} value={password}/>

                <div style={{marginTop: -10, marginBottom: 10}}>
                    <div className="row">
                        <div className="col-md-6">
                            <Link to="/forgot/password/help">Olvido credenciales?</Link>
                        </div>
                        <div className="col-md-6">
                            <div className="pull-right">
                                <Link to="/registration">No posee cuenta?</Link>
                            </div>
                        </div>
                    </div>
                </div>


                <div className="row">
                    <div className="col-md-12">
                        <Button className="login-button" bsStyle="primary" type="submit">Entrar</Button>
                    </div>
                </div>

            </form>);
    }

    login(e) {
        let creds = Immutable.fromJS(this.props.form).toJS();
        e.preventDefault();

        this.props.login(creds, (nextPath) => {
                this.props.history.push(nextPath)
            },
            () => {
                alert("Error autenticando");
            })
    }

}

function mapStateToProps(store) {
    return {
        form: store.profile.authentication,
        invalidCredentials: store.profile.invalidCredentials,
        errorAtLogin: store.profile.errorAtLogin,
        clean: store.profile.clean
    };
}

function mapDispatchToActions(dispatch) {
    return bindActionCreators({clean, login, cleanLogin, toggleCleanTimeout}, dispatch)
}

export default connect(mapStateToProps, mapDispatchToActions)(Authentication);