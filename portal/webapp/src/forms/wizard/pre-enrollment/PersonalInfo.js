/**
 * Created by fran on 1/25/18.
 */
import React, {Component} from "react";
import CodeSelect from "../../../components/CodeSelect";
import DateInput from "../../../components/DateInput";
import TextInput from "../../../components/TextInput";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {loadPersonalInfo, savePreEnrollment} from "../../../redux/actions";
import entrollmentIllustration from "../.././../style/img/entrollment-illustration.png";

class PersonalInfo extends Component {

    constructor(props) {
        super(props);
        this.inputHandler = this.inputHandler.bind(this);
        this.onValidDate = this.onValidDate.bind(this);
    }

    componentWillMount() {
        this.props.loadPersonalInfo(() => {
        }, () => {
            alert("Ocurrio un error buscando la solicitud");
        });
    }

    inputHandler(e) {
        let form = this.props.student;
        let element = e.target;
        form[element.id] = element.value;
    }

    onValidDate(date) {
        let form = this.props.student;
        form.dateOfBirth = date;
    }


    onPress(onResult, onError) {
        let form = this.props.student;
        this.props.savePreEnrollment(form, onResult, onError);
    }

    render() {
        let student = this.props.student;
        let studentExists = this.props.found;
        return [<div className="col-md-7 content-section">
            <div className="title">
                <div className="description mb40"><h2 className="f90sbg">OK.</h2>
                    <div className="violet-line"></div>
                </div>
                <span className="f20slg"><span className="f30slg">Let’s <span className="f30slb">Pre-register</span> the student in the system</span></span>
            </div>
            <div className="body d-flex align-items-center flex-column justify-content-end">
                <form className="mt50">
                    <div className="row plr15 pb50">
                        <div className="col-md-3 pl-0 pr50">
                            <TextInput id="firstName" type="name" placeholder="Nombre"
                                       value={student.firstName}
                                       onChange={this.inputHandler}
                                       disabled={studentExists}/>
                        </div>
                        <div className="col-md-3 pl-0 pr50">
                            <TextInput id="middleName" type="name" placeholder="Segundo Nombre"
                                       required={false}
                                       value={student.middleName}
                                       onChange={this.inputHandler}
                                       disabled={studentExists}/>
                        </div>
                        <div className="col-md-6 pl-0 pr50">
                            <TextInput id="fatherLastName" type="lastname" placeholder="Apellidos"
                                       value={student.fatherLastName}
                                       onChange={this.inputHandler}
                                       disabled={studentExists}/>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6">
                            <CodeSelect id="gender"
                                        label="Genero"
                                        codeType="gender"
                                        value={student.gender}
                                        onChange={this.inputHandler}
                                        placeholder="Seleccione su Genero"
                                        disabled={studentExists}
                            />

                        </div>
                        {/*<div className="col-md-6">*/}
                            {/*<DateInput id="dateOfBirth" label="Fecha de Nacimiento"*/}
                                       {/*value={student.dateOfBirth}*/}
                                       {/*onValidDate={this.onValidDate}*/}
                                       {/*disabled={studentExists}/>*/}
                        {/*</div>*/}
                    </div>
                    {this.props.footer}
                </form>
            </div>
        </div>,
            <div className="col-md-4 illustration-section d-flex align-items-center text-center">
                <div className="illustration"><img src={entrollmentIllustration} alt=""/></div>
            </div>];
    }

    renderOld() {
        let student = this.props.student;
        let studentExists = this.props.found;
        return (
            <form>
                <div className="row">
                    <div className="col-md-3">
                        <TextInput id="firstName" type="name" placeholder="Nombre"
                                   value={student.firstName}
                                   onChange={this.inputHandler}
                                   disabled={studentExists}/>
                    </div>
                    <div className="col-md-3">
                        <TextInput id="middleName" type="name" placeholder="Segundo Nombre"
                                   required={false}
                                   value={student.middleName}
                                   onChange={this.inputHandler}
                                   disabled={studentExists}/>
                    </div>
                    <div className="col-md-3">
                        <TextInput id="fatherLastName" type="lastname" placeholder="Apellido Paternal"
                                   value={student.fatherLastName}
                                   onChange={this.inputHandler}
                                   disabled={studentExists}/>
                    </div>
                    <div className="col-md-3">
                        <TextInput id="motherLastName" type="lastname" placeholder="Apellido Maternal"
                                   value={student.motherLastName}
                                   onChange={this.inputHandler}
                                   disabled={studentExists}/>
                    </div>

                </div>

                <div className="row">
                    <div className="col-md-6">
                        <CodeSelect id="gender"
                                    label="Genero"
                                    codeType="gender"
                                    value={student.gender}
                                    onChange={this.inputHandler}
                                    placeholder="Seleccione su Genero"
                                    disabled={studentExists}
                        />

                    </div>
                    <div className="col-md-6">
                        <DateInput id="dateOfBirth" label="Fecha de Nacimiento"
                                   value={student.dateOfBirth}
                                   onValidDate={this.onValidDate}
                                   disabled={studentExists}/>
                    </div>
                </div>
            </form>
        );
    }
}


function mapStateToProps(store) {
    return {
        student: store.studentInfo.student,
        found: store.studentLookup.found
    };
}

function mapDispatchToActions(dispatch) {
    return bindActionCreators({loadPersonalInfo, savePreEnrollment}, dispatch)
}

export default connect(mapStateToProps, mapDispatchToActions, null, {withRef: true})(PersonalInfo);

