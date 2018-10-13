import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item-sub-group.reducer';
import { IItemSubGroup } from 'app/shared/model/item-sub-group.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemSubGroupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ItemSubGroupDetail extends React.Component<IItemSubGroupDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { itemSubGroupEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="tpwebsiteApp.itemSubGroup.detail.title">ItemSubGroup</Translate> [<b>{itemSubGroupEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemSubGroupName">
                <Translate contentKey="tpwebsiteApp.itemSubGroup.itemSubGroupName">Item Sub Group Name</Translate>
              </span>
            </dt>
            <dd>{itemSubGroupEntity.itemSubGroupName}</dd>
            <dt>
              <span id="itemSubGroupDescription">
                <Translate contentKey="tpwebsiteApp.itemSubGroup.itemSubGroupDescription">Item Sub Group Description</Translate>
              </span>
            </dt>
            <dd>{itemSubGroupEntity.itemSubGroupDescription}</dd>
            <dt>
              <Translate contentKey="tpwebsiteApp.itemSubGroup.itemGroup">Item Group</Translate>
            </dt>
            <dd>{itemSubGroupEntity.itemGroup ? itemSubGroupEntity.itemGroup.itemGroupName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/item-sub-group" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/item-sub-group/${itemSubGroupEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ itemSubGroup }: IRootState) => ({
  itemSubGroupEntity: itemSubGroup.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemSubGroupDetail);
