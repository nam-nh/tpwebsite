import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IItemSubGroup } from 'app/shared/model/item-sub-group.model';
import { getEntities as getItemSubGroups } from 'app/entities/item-sub-group/item-sub-group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IItemUpdateState {
  isNew: boolean;
  itemSubGroupId: string;
}

export class ItemUpdate extends React.Component<IItemUpdateProps, IItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      itemSubGroupId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getItemSubGroups();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { itemEntity } = this.props;
      const entity = {
        ...itemEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/item');
  };

  render() {
    const { itemEntity, itemSubGroups, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="tpwebsiteApp.item.home.createOrEditLabel">
              <Translate contentKey="tpwebsiteApp.item.home.createOrEditLabel">Create or edit a Item</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : itemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="itemNameLabel" for="itemName">
                    <Translate contentKey="tpwebsiteApp.item.itemName">Item Name</Translate>
                  </Label>
                  <AvField
                    id="item-itemName"
                    type="text"
                    name="itemName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="itemImageLabel" for="itemImage">
                    <Translate contentKey="tpwebsiteApp.item.itemImage">Item Image</Translate>
                  </Label>
                  <AvField
                    id="item-itemImage"
                    type="text"
                    name="itemImage"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="itemDescriptionLabel" for="itemDescription">
                    <Translate contentKey="tpwebsiteApp.item.itemDescription">Item Description</Translate>
                  </Label>
                  <AvField id="item-itemDescription" type="text" name="itemDescription" />
                </AvGroup>
                <AvGroup>
                  <Label id="itemQuantityLabel" for="itemQuantity">
                    <Translate contentKey="tpwebsiteApp.item.itemQuantity">Item Quantity</Translate>
                  </Label>
                  <AvField
                    id="item-itemQuantity"
                    type="string"
                    className="form-control"
                    name="itemQuantity"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="itemPriceLabel" for="itemPrice">
                    <Translate contentKey="tpwebsiteApp.item.itemPrice">Item Price</Translate>
                  </Label>
                  <AvField
                    id="item-itemPrice"
                    type="string"
                    className="form-control"
                    name="itemPrice"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="itemSubGroup.id">
                    <Translate contentKey="tpwebsiteApp.item.itemSubGroup">Item Sub Group</Translate>
                  </Label>
                  <AvInput id="item-itemSubGroup" type="select" className="form-control" name="itemSubGroup.id">
                    <option value="" key="0" />
                    {itemSubGroups
                      ? itemSubGroups.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.itemSubGroupName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/item" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  itemSubGroups: storeState.itemSubGroup.entities,
  itemEntity: storeState.item.entity,
  loading: storeState.item.loading,
  updating: storeState.item.updating
});

const mapDispatchToProps = {
  getItemSubGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemUpdate);
