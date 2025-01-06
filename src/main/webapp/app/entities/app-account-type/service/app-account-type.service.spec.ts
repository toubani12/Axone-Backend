import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppAccountType } from '../app-account-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../app-account-type.test-samples';

import { AppAccountTypeService } from './app-account-type.service';

const requireRestSample: IAppAccountType = {
  ...sampleWithRequiredData,
};

describe('AppAccountType Service', () => {
  let service: AppAccountTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppAccountType | IAppAccountType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppAccountTypeService);
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

    it('should create a AppAccountType', () => {
      const appAccountType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appAccountType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppAccountType', () => {
      const appAccountType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appAccountType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AppAccountType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppAccountType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AppAccountType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppAccountTypeToCollectionIfMissing', () => {
      it('should add a AppAccountType to an empty array', () => {
        const appAccountType: IAppAccountType = sampleWithRequiredData;
        expectedResult = service.addAppAccountTypeToCollectionIfMissing([], appAccountType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appAccountType);
      });

      it('should not add a AppAccountType to an array that contains it', () => {
        const appAccountType: IAppAccountType = sampleWithRequiredData;
        const appAccountTypeCollection: IAppAccountType[] = [
          {
            ...appAccountType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppAccountTypeToCollectionIfMissing(appAccountTypeCollection, appAccountType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppAccountType to an array that doesn't contain it", () => {
        const appAccountType: IAppAccountType = sampleWithRequiredData;
        const appAccountTypeCollection: IAppAccountType[] = [sampleWithPartialData];
        expectedResult = service.addAppAccountTypeToCollectionIfMissing(appAccountTypeCollection, appAccountType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appAccountType);
      });

      it('should add only unique AppAccountType to an array', () => {
        const appAccountTypeArray: IAppAccountType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appAccountTypeCollection: IAppAccountType[] = [sampleWithRequiredData];
        expectedResult = service.addAppAccountTypeToCollectionIfMissing(appAccountTypeCollection, ...appAccountTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appAccountType: IAppAccountType = sampleWithRequiredData;
        const appAccountType2: IAppAccountType = sampleWithPartialData;
        expectedResult = service.addAppAccountTypeToCollectionIfMissing([], appAccountType, appAccountType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appAccountType);
        expect(expectedResult).toContain(appAccountType2);
      });

      it('should accept null and undefined values', () => {
        const appAccountType: IAppAccountType = sampleWithRequiredData;
        expectedResult = service.addAppAccountTypeToCollectionIfMissing([], null, appAccountType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appAccountType);
      });

      it('should return initial array if no AppAccountType is added', () => {
        const appAccountTypeCollection: IAppAccountType[] = [sampleWithRequiredData];
        expectedResult = service.addAppAccountTypeToCollectionIfMissing(appAccountTypeCollection, undefined, null);
        expect(expectedResult).toEqual(appAccountTypeCollection);
      });
    });

    describe('compareAppAccountType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppAccountType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppAccountType(entity1, entity2);
        const compareResult2 = service.compareAppAccountType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppAccountType(entity1, entity2);
        const compareResult2 = service.compareAppAccountType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppAccountType(entity1, entity2);
        const compareResult2 = service.compareAppAccountType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
