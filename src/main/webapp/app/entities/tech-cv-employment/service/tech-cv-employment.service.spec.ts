import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITechCVEmployment } from '../tech-cv-employment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tech-cv-employment.test-samples';

import { TechCVEmploymentService } from './tech-cv-employment.service';

const requireRestSample: ITechCVEmployment = {
  ...sampleWithRequiredData,
};

describe('TechCVEmployment Service', () => {
  let service: TechCVEmploymentService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechCVEmployment | ITechCVEmployment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TechCVEmploymentService);
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

    it('should create a TechCVEmployment', () => {
      const techCVEmployment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(techCVEmployment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TechCVEmployment', () => {
      const techCVEmployment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(techCVEmployment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TechCVEmployment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TechCVEmployment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TechCVEmployment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTechCVEmploymentToCollectionIfMissing', () => {
      it('should add a TechCVEmployment to an empty array', () => {
        const techCVEmployment: ITechCVEmployment = sampleWithRequiredData;
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing([], techCVEmployment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVEmployment);
      });

      it('should not add a TechCVEmployment to an array that contains it', () => {
        const techCVEmployment: ITechCVEmployment = sampleWithRequiredData;
        const techCVEmploymentCollection: ITechCVEmployment[] = [
          {
            ...techCVEmployment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing(techCVEmploymentCollection, techCVEmployment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TechCVEmployment to an array that doesn't contain it", () => {
        const techCVEmployment: ITechCVEmployment = sampleWithRequiredData;
        const techCVEmploymentCollection: ITechCVEmployment[] = [sampleWithPartialData];
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing(techCVEmploymentCollection, techCVEmployment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVEmployment);
      });

      it('should add only unique TechCVEmployment to an array', () => {
        const techCVEmploymentArray: ITechCVEmployment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const techCVEmploymentCollection: ITechCVEmployment[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing(techCVEmploymentCollection, ...techCVEmploymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const techCVEmployment: ITechCVEmployment = sampleWithRequiredData;
        const techCVEmployment2: ITechCVEmployment = sampleWithPartialData;
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing([], techCVEmployment, techCVEmployment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVEmployment);
        expect(expectedResult).toContain(techCVEmployment2);
      });

      it('should accept null and undefined values', () => {
        const techCVEmployment: ITechCVEmployment = sampleWithRequiredData;
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing([], null, techCVEmployment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVEmployment);
      });

      it('should return initial array if no TechCVEmployment is added', () => {
        const techCVEmploymentCollection: ITechCVEmployment[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVEmploymentToCollectionIfMissing(techCVEmploymentCollection, undefined, null);
        expect(expectedResult).toEqual(techCVEmploymentCollection);
      });
    });

    describe('compareTechCVEmployment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechCVEmployment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTechCVEmployment(entity1, entity2);
        const compareResult2 = service.compareTechCVEmployment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTechCVEmployment(entity1, entity2);
        const compareResult2 = service.compareTechCVEmployment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTechCVEmployment(entity1, entity2);
        const compareResult2 = service.compareTechCVEmployment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
