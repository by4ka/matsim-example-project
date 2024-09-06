package org.matsim.analysis;

import org.geotools.data.shapefile.shp.ShapefileReader;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.locationtech.jts.geom.Geometry;

import java.util.stream.Collectors;

public class CountActivitiesInMitte {
    public static void main(String[] args) {
        var ShapeFileName = "/Users/shakshuka/Desktop/Bachelor/neu/Daten/Berlin_Bezirksgrenzen_8674208815691846478/Berlin_Bezirke.shp";
        var plansFileName = "/Users/shakshuka/Downloads/berlin-v5.5.3-1pct.output_plans.xml";
        var transformation = TransformationFactory.getCoordinateTransformation("EPSG:31468", "EPSG:3857");

        var features = ShapeFileReader.getAllFeatures(ShapeFileName);

        var geometries = features.stream()
                .filter(simpleFeature -> simpleFeature.getAttribute("Gemeinde_s").equals("001"))
                .map(simpleFeature -> (Geometry) simpleFeature.getDefaultGeometry())
                .collect(Collectors.toList());

        var mitteGeometry = geometries.get(0);

        var population = PopulationUtils.readPopulation(plansFileName);

        var counter = 0;

        for (Person person: population.getPersons().values()) {

            var plan = person.getSelectedPlan();
            var activities = TripStructureUtils.getActivities(plan, TripStructureUtils.StageActivityHandling.ExcludeStageActivities);

            for (Activity activity : activities) {

                var coord = activity.getCoord();
                var transformedCoord = transformation.transform(coord);
                var geotoolsPoint = MGC.coord2Point(transformedCoord);

                if (mitteGeometry.contains(geotoolsPoint)) {
                    counter++;
                }
            }
        }
    System.out.println(counter + " activities in Mitte.");
    }

}

