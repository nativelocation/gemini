import React, {Component} from "react";
import AnimationHelper from "../components/AnimationHelper";
import * as UIHelper from "../UIHelper";

export default class Welcome extends Component {

    constructor(props) {
        super(props);
        this.goAuthentication = this.goAuthentication.bind(this);
    }

    goAuthentication() {
        this.props.history.push("/login");
    }

    render() {
        return [<div className="col-md-5 content-section">
            <div className="title">
                <h2>{UIHelper.getText("projectName")}</h2>
                <div className="description"><p>{UIHelper.getText("projectNameFragment")}</p>
                    <div className="green-line"/>
                </div>

                <p className="f20slg text-justify">
                    {UIHelper.getText("welcomePage")}<span className="f20slb">{UIHelper.getText("enrollmentYear")}</span>.
                </p>
            </div>
            <div className="body d-flex align-items-center flex-column justify-content-end">
                <p>{UIHelper.getText("startDescription")}</p>
                <a onClick={this.goAuthentication} className="button-blue">{UIHelper.getText("welcomeStartButton")}</a>
            </div>
        </div>,
            <div className="col-md-6 illustration-section d-flex align-items-center text-center">
                {/*<div className="illustration"><img src={registrationIllustration} alt=""/></div>*/}
                <AnimationHelper type="home"/>
            </div>
        ];
    }
}