import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../tech-cv-alt-activities.test-samples';

import { TechCVAltActivitiesService } from './tech-cv-alt-activities.service';

const requireRestSample: ITechCVAltActivities = {
  ...sampleWithRequiredData,
};

describe('TechCVAltActivities Service', () => {
  let service: TechCVAltActivitiesService;
  let httpMock: HttpTestingController;
  let expectedResult: ITechCVAltActivities | ITechCVAltActivities[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TechCVAltActivitiesService);
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

    it('should create a TechCVAltActivities', () => {
      const techCVAltActivities = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(techCVAltActivities).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TechCVAltActivities', () => {
      const techCVAltActivities = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(techCVAltActivities).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TechCVAltActivities', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TechCVAltActivities', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TechCVAltActivities', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTechCVAltActivitiesToCollectionIfMissing', () => {
      it('should add a TechCVAltActivities to an empty array', () => {
        const techCVAltActivities: ITechCVAltActivities = sampleWithRequiredData;
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing([], techCVAltActivities);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVAltActivities);
      });

      it('should not add a TechCVAltActivities to an array that contains it', () => {
        const techCVAltActivities: ITechCVAltActivities = sampleWithRequiredData;
        const techCVAltActivitiesCollection: ITechCVAltActivities[] = [
          {
            ...techCVAltActivities,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing(techCVAltActivitiesCollection, techCVAltActivities);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TechCVAltActivities to an array that doesn't contain it", () => {
        const techCVAltActivities: ITechCVAltActivities = sampleWithRequiredData;
        const techCVAltActivitiesCollection: ITechCVAltActivities[] = [sampleWithPartialData];
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing(techCVAltActivitiesCollection, techCVAltActivities);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVAltActivities);
      });

      it('should add only unique TechCVAltActivities to an array', () => {
        const techCVAltActivitiesArray: ITechCVAltActivities[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const techCVAltActivitiesCollection: ITechCVAltActivities[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing(techCVAltActivitiesCollection, ...techCVAltActivitiesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const techCVAltActivities: ITechCVAltActivities = sampleWithRequiredData;
        const techCVAltActivities2: ITechCVAltActivities = sampleWithPartialData;
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing([], techCVAltActivities, techCVAltActivities2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(techCVAltActivities);
        expect(expectedResult).toContain(techCVAltActivities2);
      });

      it('should accept null and undefined values', () => {
        const techCVAltActivities: ITechCVAltActivities = sampleWithRequiredData;
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing([], null, techCVAltActivities, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(techCVAltActivities);
      });

      it('should return initial array if no TechCVAltActivities is added', () => {
        const techCVAltActivitiesCollection: ITechCVAltActivities[] = [sampleWithRequiredData];
        expectedResult = service.addTechCVAltActivitiesToCollectionIfMissing(techCVAltActivitiesCollection, undefined, null);
        expect(expectedResult).toEqual(techCVAltActivitiesCollection);
      });
    });

    describe('compareTechCVAltActivities', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTechCVAltActivities(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTechCVAltActivities(entity1, entity2);
        const compareResult2 = service.compareTechCVAltActivities(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTechCVAltActivities(entity1, entity2);
        const compareResult2 = service.compareTechCVAltActivities(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTechCVAltActivities(entity1, entity2);
        const compareResult2 = service.compareTechCVAltActivities(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
