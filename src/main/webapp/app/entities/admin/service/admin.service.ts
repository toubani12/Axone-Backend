import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdmin, NewAdmin } from '../admin.model';

export type PartialUpdateAdmin = Partial<IAdmin> & Pick<IAdmin, 'id'>;

export type EntityResponseType = HttpResponse<IAdmin>;
export type EntityArrayResponseType = HttpResponse<IAdmin[]>;

@Injectable({ providedIn: 'root' })
export class AdminService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/admins');

  create(admin: NewAdmin): Observable<EntityResponseType> {
    return this.http.post<IAdmin>(this.resourceUrl, admin, { observe: 'response' });
  }

  update(admin: IAdmin): Observable<EntityResponseType> {
    return this.http.put<IAdmin>(`${this.resourceUrl}/${this.getAdminIdentifier(admin)}`, admin, { observe: 'response' });
  }

  partialUpdate(admin: PartialUpdateAdmin): Observable<EntityResponseType> {
    return this.http.patch<IAdmin>(`${this.resourceUrl}/${this.getAdminIdentifier(admin)}`, admin, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdmin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdmin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdminIdentifier(admin: Pick<IAdmin, 'id'>): number {
    return admin.id;
  }

  compareAdmin(o1: Pick<IAdmin, 'id'> | null, o2: Pick<IAdmin, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdminIdentifier(o1) === this.getAdminIdentifier(o2) : o1 === o2;
  }

  addAdminToCollectionIfMissing<Type extends Pick<IAdmin, 'id'>>(
    adminCollection: Type[],
    ...adminsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const admins: Type[] = adminsToCheck.filter(isPresent);
    if (admins.length > 0) {
      const adminCollectionIdentifiers = adminCollection.map(adminItem => this.getAdminIdentifier(adminItem));
      const adminsToAdd = admins.filter(adminItem => {
        const adminIdentifier = this.getAdminIdentifier(adminItem);
        if (adminCollectionIdentifiers.includes(adminIdentifier)) {
          return false;
        }
        adminCollectionIdentifiers.push(adminIdentifier);
        return true;
      });
      return [...adminsToAdd, ...adminCollection];
    }
    return adminCollection;
  }
}
