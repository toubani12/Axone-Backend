import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppTestType } from '../app-test-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../app-test-type.test-samples';

import { AppTestTypeService } from './app-test-type.service';

const requireRestSample: IAppTestType = {
  ...sampleWithRequiredData,
};

describe('AppTestType Service', () => {
  let service: AppTestTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppTestType | IAppTestType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppTestTypeService);
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

    it('should create a AppTestType', () => {
      const appTestType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appTestType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppTestType', () => {
      const appTestType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appTestType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AppTestType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppTestType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AppTestType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppTestTypeToCollectionIfMissing', () => {
      it('should add a AppTestType to an empty array', () => {
        const appTestType: IAppTestType = sampleWithRequiredData;
        expectedResult = service.addAppTestTypeToCollectionIfMissing([], appTestType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appTestType);
      });

      it('should not add a AppTestType to an array that contains it', () => {
        const appTestType: IAppTestType = sampleWithRequiredData;
        const appTestTypeCollection: IAppTestType[] = [
          {
            ...appTestType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppTestTypeToCollectionIfMissing(appTestTypeCollection, appTestType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppTestType to an array that doesn't contain it", () => {
        const appTestType: IAppTestType = sampleWithRequiredData;
        const appTestTypeCollection: IAppTestType[] = [sampleWithPartialData];
        expectedResult = service.addAppTestTypeToCollectionIfMissing(appTestTypeCollection, appTestType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appTestType);
      });

      it('should add only unique AppTestType to an array', () => {
        const appTestTypeArray: IAppTestType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appTestTypeCollection: IAppTestType[] = [sampleWithRequiredData];
        expectedResult = service.addAppTestTypeToCollectionIfMissing(appTestTypeCollection, ...appTestTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appTestType: IAppTestType = sampleWithRequiredData;
        const appTestType2: IAppTestType = sampleWithPartialData;
        expectedResult = service.addAppTestTypeToCollectionIfMissing([], appTestType, appTestType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appTestType);
        expect(expectedResult).toContain(appTestType2);
      });

      it('should accept null and undefined values', () => {
        const appTestType: IAppTestType = sampleWithRequiredData;
        expectedResult = service.addAppTestTypeToCollectionIfMissing([], null, appTestType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appTestType);
      });

      it('should return initial array if no AppTestType is added', () => {
        const appTestTypeCollection: IAppTestType[] = [sampleWithRequiredData];
        expectedResult = service.addAppTestTypeToCollectionIfMissing(appTestTypeCollection, undefined, null);
        expect(expectedResult).toEqual(appTestTypeCollection);
      });
    });

    describe('compareAppTestType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppTestType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppTestType(entity1, entity2);
        const compareResult2 = service.compareAppTestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppTestType(entity1, entity2);
        const compareResult2 = service.compareAppTestType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppTestType(entity1, entity2);
        const compareResult2 = service.compareAppTestType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
