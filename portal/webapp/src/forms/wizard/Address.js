/**
 * Created by fran on 1/26/18.
 */
import React, {Component} from "react";
import {Button} from "react-bootstrap";
import CodeSelect from "../../components/CodeSelect";
import TextInput from "../../components/TextInput";

export default class Address extends Component {

    constructor(props) {
        super(props);
        this.state = {
            residential: {line1: '', line2: '', city: '', state: '', zipcode: ''},
            postal: {line1: '', line2: '', city: '', state: '', zipcode: ''}
        };
        this.inputHandler = this.inputHandler.bind(this);
        this.copyAddress = this.copyAddress.bind(this);
    }

    inputHandler(e) {
        let form = {...this.state};
        let element = e.target;
        let tokens = element.id.split(".");
        let context = tokens[0];
        let id = tokens[1];

        form[context][id] = element.value;
        // if(element.id === `${type}.city`) {
        //     let index = element.selectedIndex;
        //     form.personal.relationTypeDesc = element[index].text;
        // }

        this.setState({...this.state});
    }

    copyAddress(e) {
        let residential = Object.assign({}, this.state.residential);
        this.setState({...this.state, postal: residential});
    }


    render() {
        let state = {...this.state};
        return (<form>

                <div className="row">
                    <div className="col-md-6">
                        <div className="row">
                            <div className="col-md-2">
                                <h5 htmlFor="">Residencial</h5>
                            </div>

                            <div className="col-md-3" style={{marginLeft: 10}}>
                                <Button onClick={this.copyAddress}
                                        bsSize="small"
                                        bsStyle="primary">Copiar residencial a postal</Button>
                            </div>

                        </div>
                        {this.renderAddressForm("residential", state.residential)}

                    </div>

                    <div className="col-md-6">
                        <div className="row">
                            <div className="col-md-12">
                                <div className="form-group">
                                    <h5 htmlFor="">Postal</h5>
                                </div>
                            </div>
                        </div>
                        {this.renderAddressForm("postal", state.postal)}

                    </div>
                </div>

            </form>
        );
    }

    renderAddressForm(type, address) {
        return (<div>
            <div className="row">
                <div className="col-md-12">
                    <TextInput type="addressLine" id={`${type}.line1`} label="Linea 1" placeholder="Linea 1"
                               value={address.line1} onChange={this.inputHandler}/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <TextInput type="addressLine" id={`${type}.line2`} label="Linea 2" placeholder="Linea 2"
                               value={address.line2} onChange={this.inputHandler}/>
                </div>
            </div>
            <div className="row">
                <div className="col-md-4">
                    <CodeSelect id={`${type}.city`} label="Ciudad" placeholder="Seleccione municipio" codeType="municipios"
                                value={address.city} onChange={this.inputHandler}/>
                </div>
                <div className="col-md-4">
                    <CodeSelect id={`${type}.state`} label="Estado" placeholder="Seleccione Estado" codeType="states" value={"PR"}
                                disabled={false} onChange={this.inputHandler}/>
                </div>

                <div className="col-md-4">
                    <TextInput type="zipcode" id={`${type}.zipcode`} label="Codigo Postal" placeholder="Zip Code"
                               value={address.zipcode} onChange={this.inputHandler}/>
                </div>
            </div>
        </div>);
    }
}