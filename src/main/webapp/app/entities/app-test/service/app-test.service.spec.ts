import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppTest } from '../app-test.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../app-test.test-samples';

import { AppTestService } from './app-test.service';

const requireRestSample: IAppTest = {
  ...sampleWithRequiredData,
};

describe('AppTest Service', () => {
  let service: AppTestService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppTest | IAppTest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppTestService);
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

    it('should create a AppTest', () => {
      const appTest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AppTest', () => {
      const appTest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appTest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AppTest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AppTest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AppTest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppTestToCollectionIfMissing', () => {
      it('should add a AppTest to an empty array', () => {
        const appTest: IAppTest = sampleWithRequiredData;
        expectedResult = service.addAppTestToCollectionIfMissing([], appTest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appTest);
      });

      it('should not add a AppTest to an array that contains it', () => {
        const appTest: IAppTest = sampleWithRequiredData;
        const appTestCollection: IAppTest[] = [
          {
            ...appTest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppTestToCollectionIfMissing(appTestCollection, appTest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AppTest to an array that doesn't contain it", () => {
        const appTest: IAppTest = sampleWithRequiredData;
        const appTestCollection: IAppTest[] = [sampleWithPartialData];
        expectedResult = service.addAppTestToCollectionIfMissing(appTestCollection, appTest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appTest);
      });

      it('should add only unique AppTest to an array', () => {
        const appTestArray: IAppTest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appTestCollection: IAppTest[] = [sampleWithRequiredData];
        expectedResult = service.addAppTestToCollectionIfMissing(appTestCollection, ...appTestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appTest: IAppTest = sampleWithRequiredData;
        const appTest2: IAppTest = sampleWithPartialData;
        expectedResult = service.addAppTestToCollectionIfMissing([], appTest, appTest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appTest);
        expect(expectedResult).toContain(appTest2);
      });

      it('should accept null and undefined values', () => {
        const appTest: IAppTest = sampleWithRequiredData;
        expectedResult = service.addAppTestToCollectionIfMissing([], null, appTest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appTest);
      });

      it('should return initial array if no AppTest is added', () => {
        const appTestCollection: IAppTest[] = [sampleWithRequiredData];
        expectedResult = service.addAppTestToCollectionIfMissing(appTestCollection, undefined, null);
        expect(expectedResult).toEqual(appTestCollection);
      });
    });

    describe('compareAppTest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppTest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppTest(entity1, entity2);
        const compareResult2 = service.compareAppTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppTest(entity1, entity2);
        const compareResult2 = service.compareAppTest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppTest(entity1, entity2);
        const compareResult2 = service.compareAppTest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
