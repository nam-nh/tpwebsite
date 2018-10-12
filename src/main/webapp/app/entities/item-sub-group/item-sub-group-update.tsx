import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IItemGroup } from 'app/shared/model/item-group.model';
import { getEntities as getItemGroups } from 'app/entities/item-group/item-group.reducer';
import { getEntity, updateEntity, createEntity, reset } from './item-sub-group.reducer';
import { IItemSubGroup } from 'app/shared/model/item-sub-group.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IItemSubGroupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IItemSubGroupUpdateState {
  isNew: boolean;
  itemGroupId: string;
}

export class ItemSubGroupUpdate extends React.Component<IItemSubGroupUpdateProps, IItemSubGroupUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      itemGroupId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getItemGroups();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { itemSubGroupEntity } = this.props;
      const entity = {
        ...itemSubGroupEntity,
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
    this.props.history.push('/entity/item-sub-group');
  };

  render() {
    const { itemSubGroupEntity, itemGroups, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="tpwebsiteApp.itemSubGroup.home.createOrEditLabel">
              <Translate contentKey="tpwebsiteApp.itemSubGroup.home.createOrEditLabel">Create or edit a ItemSubGroup</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : itemSubGroupEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="item-sub-group-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="itemSubGroupNameLabel" for="itemSubGroupName">
                    <Translate contentKey="tpwebsiteApp.itemSubGroup.itemSubGroupName">Item Sub Group Name</Translate>
                  </Label>
                  <AvField
                    id="item-sub-group-itemSubGroupName"
                    type="text"
                    name="itemSubGroupName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="itemSubGroupDescriptionLabel" for="itemSubGroupDescription">
                    <Translate contentKey="tpwebsiteApp.itemSubGroup.itemSubGroupDescription">Item Sub Group Description</Translate>
                  </Label>
                  <AvField id="item-sub-group-itemSubGroupDescription" type="text" name="itemSubGroupDescription" />
                </AvGroup>
                <AvGroup>
                  <Label for="itemGroup.id">
                    <Translate contentKey="tpwebsiteApp.itemSubGroup.itemGroup">Item Group</Translate>
                  </Label>
                  <AvInput id="item-sub-group-itemGroup" type="select" className="form-control" name="itemGroup.id">
                    <option value="" key="0" />
                    {itemGroups
                      ? itemGroups.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/item-sub-group" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
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
  itemGroups: storeState.itemGroup.entities,
  itemSubGroupEntity: storeState.itemSubGroup.entity,
  loading: storeState.itemSubGroup.loading,
  updating: storeState.itemSubGroup.updating
});

const mapDispatchToProps = {
  getItemGroups,
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
)(ItemSubGroupUpdate);
