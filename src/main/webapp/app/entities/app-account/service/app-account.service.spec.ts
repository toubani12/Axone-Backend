import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAppAccount } from '../app-account.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../app-account.test-samples';

import { AppAccountService, RestAppAccount } from './app-account.service';

const requireRestSample: RestAppAccount = {
  ...sampleWithRequiredData,
  endDate: sampleWithRequiredData.endDate?.format(DATE_FORMAT),
};

describe('AppAccount Service', () => {
  let service: AppAccountService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppAccount | IAppAccount[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppAccountService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AppAccount', () => {
      const appAccount = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppAccount', () => {
      const appAccount = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appAccount).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AppAccount', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppAccount', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AppAccount', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppAccountToCollectionIfMissing', () => {
      it('should add a AppAccount to an empty array', () => {
        const appAccount: IAppAccount = sampleWithRequiredData;
        expectedResult = service.addAppAccountToCollectionIfMissing([], appAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appAccount);
      });

      it('should not add a AppAccount to an array that contains it', () => {
        const appAccount: IAppAccount = sampleWithRequiredData;
        const appAccountCollection: IAppAccount[] = [
          {
            ...appAccount,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppAccountToCollectionIfMissing(appAccountCollection, appAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppAccount to an array that doesn't contain it", () => {
        const appAccount: IAppAccount = sampleWithRequiredData;
        const appAccountCollection: IAppAccount[] = [sampleWithPartialData];
        expectedResult = service.addAppAccountToCollectionIfMissing(appAccountCollection, appAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appAccount);
      });

      it('should add only unique AppAccount to an array', () => {
        const appAccountArray: IAppAccount[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appAccountCollection: IAppAccount[] = [sampleWithRequiredData];
        expectedResult = service.addAppAccountToCollectionIfMissing(appAccountCollection, ...appAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appAccount: IAppAccount = sampleWithRequiredData;
        const appAccount2: IAppAccount = sampleWithPartialData;
        expectedResult = service.addAppAccountToCollectionIfMissing([], appAccount, appAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appAccount);
        expect(expectedResult).toContain(appAccount2);
      });

      it('should accept null and undefined values', () => {
        const appAccount: IAppAccount = sampleWithRequiredData;
        expectedResult = service.addAppAccountToCollectionIfMissing([], null, appAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appAccount);
      });

      it('should return initial array if no AppAccount is added', () => {
        const appAccountCollection: IAppAccount[] = [sampleWithRequiredData];
        expectedResult = service.addAppAccountToCollectionIfMissing(appAccountCollection, undefined, null);
        expect(expectedResult).toEqual(appAccountCollection);
      });
    });

    describe('compareAppAccount', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppAccount(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppAccount(entity1, entity2);
        const compareResult2 = service.compareAppAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppAccount(entity1, entity2);
        const compareResult2 = service.compareAppAccount(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppAccount(entity1, entity2);
        const compareResult2 = service.compareAppAccount(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
