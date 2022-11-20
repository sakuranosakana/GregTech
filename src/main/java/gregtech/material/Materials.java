package gregtech.material;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialFlag;
import gregtech.apiOld.unification.material.MarkerMaterial;
import gregtech.apiOld.unification.material.MarkerMaterials;
import gregtech.material.registration.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static gregtech.core.material.MaterialFlags.*;

/**
 * Material Registration.
 * <p>
 * All Material Builders should follow this general formatting:
 * <p>
 * material = new Material.Builder(id, name)
 * .ingot().fluid().ore()                <--- types
 * .color().iconSet()                    <--- appearance
 * .flags()                              <--- special generation
 * .element() / .components()            <--- composition
 * .toolStats()                          <---
 * .oreByProducts()                         | additional properties
 * ...                                   <---
 * .blastTemp()                          <--- blast temperature
 * .build();
 * <p>
 * Use defaults to your advantage! Some defaults:
 * - iconSet: DULL
 * - color: 0xFFFFFF
 */
public class Materials {

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    public static IMaterial[] CHEMICAL_DYES;

    public static void register() {
        if (INIT.getAndSet(true)) {
            return;
        }

        MarkerMaterials.register();

        /*
         * Ranges 1-249
         */
        ElementMaterials.register();

        /*
         * Ranges 250-999
         */
        FirstDegreeMaterials.register();

        /*
         * Ranges 1000-1499
         */
        OrganicChemistryMaterials.register();

        /*
         * Ranges 1500-1999
         */
        UnknownCompositionMaterials.register();

        /*
         * Ranges 2000-2499
         */
        SecondDegreeMaterials.register();

        /*
         * Ranges 2500-2999
         */
        HigherDegreeMaterials.register();

        /*
         * Register info for cyclical references
         */
        MaterialFlagAddition.register();

        /*
         * FOR ADDON DEVELOPERS:
         *
         * GTCEu will not take more than 3000 IDs. Anything past ID 2999
         * is considered FAIR GAME, take whatever you like.
         *
         * If you would like to reserve IDs, feel free to reach out to the
         * development team and claim a range of IDs! We will mark any
         * claimed ranges below this comment. Max value is 32767.
         *
         * - Gregicality: 3000-19999
         * - Gregification: 20000-20999
         * - HtmlTech: 21000-21499
         * - GregTech Food Option: 21500-21999
         * - PCM's Ore Addon: 22000-23599
         * - MechTech: 23600-23999
         * - FREE RANGE 24000-31999
         * - Reserved for CraftTweaker: 32000-32767
         */

        CHEMICAL_DYES = new IMaterial[]{
                Materials.DyeWhite, Materials.DyeOrange,
                Materials.DyeMagenta, Materials.DyeLightBlue,
                Materials.DyeYellow, Materials.DyeLime,
                Materials.DyePink, Materials.DyeGray,
                Materials.DyeLightGray, Materials.DyeCyan,
                Materials.DyePurple, Materials.DyeBlue,
                Materials.DyeBrown, Materials.DyeGreen,
                Materials.DyeRed, Materials.DyeBlack
        };
    }

    public static final List<IMaterialFlag> STD_METAL = new ArrayList<>();
    public static final List<IMaterialFlag> EXT_METAL = new ArrayList<>();
    public static final List<IMaterialFlag> EXT2_METAL = new ArrayList<>();

    static {
        STD_METAL.add(GENERATE_PLATE);

        EXT_METAL.addAll(STD_METAL);
        EXT_METAL.addAll(Collections.singletonList(GENERATE_ROD));

        EXT2_METAL.addAll(EXT_METAL);
        EXT2_METAL.addAll(Arrays.asList(GENERATE_LONG_ROD, GENERATE_BOLT_SCREW));
    }

    public static final MarkerMaterial NULL = new MarkerMaterial("null");

    /**
     * Direct Elements
     */
    public static IMaterial Actinium;
    public static IMaterial Aluminium;
    public static IMaterial Americium;
    public static IMaterial Antimony;
    public static IMaterial Argon;
    public static IMaterial Arsenic;
    public static IMaterial Astatine;
    public static IMaterial Barium;
    public static IMaterial Berkelium;
    public static IMaterial Beryllium;
    public static IMaterial Bismuth;
    public static IMaterial Bohrium;
    public static IMaterial Boron;
    public static IMaterial Bromine;
    public static IMaterial Caesium;
    public static IMaterial Calcium;
    public static IMaterial Californium;
    public static IMaterial Carbon;
    public static IMaterial Cadmium;
    public static IMaterial Cerium;
    public static IMaterial Chlorine;
    public static IMaterial Chrome;
    public static IMaterial Cobalt;
    public static IMaterial Copernicium;
    public static IMaterial Copper;
    public static IMaterial Curium;
    public static IMaterial Darmstadtium;
    public static IMaterial Deuterium;
    public static IMaterial Dubnium;
    public static IMaterial Dysprosium;
    public static IMaterial Einsteinium;
    public static IMaterial Erbium;
    public static IMaterial Europium;
    public static IMaterial Fermium;
    public static IMaterial Flerovium;
    public static IMaterial Fluorine;
    public static IMaterial Francium;
    public static IMaterial Gadolinium;
    public static IMaterial Gallium;
    public static IMaterial Germanium;
    public static IMaterial Gold;
    public static IMaterial Hafnium;
    public static IMaterial Hassium;
    public static IMaterial Holmium;
    public static IMaterial Hydrogen;
    public static IMaterial Helium;
    public static IMaterial Helium3;
    public static IMaterial Indium;
    public static IMaterial Iodine;
    public static IMaterial Iridium;
    public static IMaterial Iron;
    public static IMaterial Krypton;
    public static IMaterial Lanthanum;
    public static IMaterial Lawrencium;
    public static IMaterial Lead;
    public static IMaterial Lithium;
    public static IMaterial Livermorium;
    public static IMaterial Lutetium;
    public static IMaterial Magnesium;
    public static IMaterial Mendelevium;
    public static IMaterial Manganese;
    public static IMaterial Meitnerium;
    public static IMaterial Mercury;
    public static IMaterial Molybdenum;
    public static IMaterial Moscovium;
    public static IMaterial Neodymium;
    public static IMaterial Neon;
    public static IMaterial Neptunium;
    public static IMaterial Nickel;
    public static IMaterial Nihonium;
    public static IMaterial Niobium;
    public static IMaterial Nitrogen;
    public static IMaterial Nobelium;
    public static IMaterial Oganesson;
    public static IMaterial Osmium;
    public static IMaterial Oxygen;
    public static IMaterial Palladium;
    public static IMaterial Phosphorus;
    public static IMaterial Polonium;
    public static IMaterial Platinum;
    public static IMaterial Plutonium239;
    public static IMaterial Plutonium241;
    public static IMaterial Potassium;
    public static IMaterial Praseodymium;
    public static IMaterial Promethium;
    public static IMaterial Protactinium;
    public static IMaterial Radon;
    public static IMaterial Radium;
    public static IMaterial Rhenium;
    public static IMaterial Rhodium;
    public static IMaterial Roentgenium;
    public static IMaterial Rubidium;
    public static IMaterial Ruthenium;
    public static IMaterial Rutherfordium;
    public static IMaterial Samarium;
    public static IMaterial Scandium;
    public static IMaterial Seaborgium;
    public static IMaterial Selenium;
    public static IMaterial Silicon;
    public static IMaterial Silver;
    public static IMaterial Sodium;
    public static IMaterial Strontium;
    public static IMaterial Sulfur;
    public static IMaterial Tantalum;
    public static IMaterial Technetium;
    public static IMaterial Tellurium;
    public static IMaterial Tennessine;
    public static IMaterial Terbium;
    public static IMaterial Thorium;
    public static IMaterial Thallium;
    public static IMaterial Thulium;
    public static IMaterial Tin;
    public static IMaterial Titanium;
    public static IMaterial Tritium;
    public static IMaterial Tungsten;
    public static IMaterial Uranium238;
    public static IMaterial Uranium235;
    public static IMaterial Vanadium;
    public static IMaterial Xenon;
    public static IMaterial Ytterbium;
    public static IMaterial Yttrium;
    public static IMaterial Zinc;
    public static IMaterial Zirconium;

    /**
     * Fantasy Elements
     */
    public static IMaterial Naquadah;
    public static IMaterial NaquadahEnriched;
    public static IMaterial Naquadria;
    public static IMaterial Neutronium;
    public static IMaterial Tritanium;
    public static IMaterial Duranium;
    public static IMaterial Trinium;

    /**
     * First Degree Compounds
     */
    public static IMaterial Almandine;
    public static IMaterial Andradite;
    public static IMaterial AnnealedCopper;
    public static IMaterial Asbestos;
    public static IMaterial Ash;
    public static IMaterial BandedIron;
    public static IMaterial BatteryAlloy;
    public static IMaterial BlueTopaz;
    public static IMaterial Bone;
    public static IMaterial Brass;
    public static IMaterial Bronze;
    public static IMaterial BrownLimonite;
    public static IMaterial Calcite;
    public static IMaterial Cassiterite;
    public static IMaterial CassiteriteSand;
    public static IMaterial Chalcopyrite;
    public static IMaterial Charcoal;
    public static IMaterial Chromite;
    public static IMaterial Cinnabar;
    public static IMaterial Water;
    public static IMaterial LiquidOxygen;
    public static IMaterial Coal;
    public static IMaterial Cobaltite;
    public static IMaterial Cooperite;
    public static IMaterial Cupronickel;
    public static IMaterial DarkAsh;
    public static IMaterial Diamond;
    public static IMaterial Electrum;
    public static IMaterial Emerald;
    public static IMaterial Galena;
    public static IMaterial Garnierite;
    public static IMaterial GreenSapphire;
    public static IMaterial Grossular;
    public static IMaterial Ice;
    public static IMaterial Ilmenite;
    public static IMaterial Rutile;
    public static IMaterial Bauxite;
    public static IMaterial Invar;
    public static IMaterial Kanthal;
    public static IMaterial Lazurite;
    public static IMaterial LiquidHelium;
    public static IMaterial Magnalium;
    public static IMaterial Magnesite;
    public static IMaterial Magnetite;
    public static IMaterial Molybdenite;
    public static IMaterial Nichrome;
    public static IMaterial NiobiumNitride;
    public static IMaterial NiobiumTitanium;
    public static IMaterial Obsidian;
    public static IMaterial Phosphate;
    public static IMaterial SterlingSilver;
    public static IMaterial RoseGold;
    public static IMaterial BlackBronze;
    public static IMaterial BismuthBronze;
    public static IMaterial Biotite;
    public static IMaterial Powellite;
    public static IMaterial Pyrite;
    public static IMaterial Pyrolusite;
    public static IMaterial Pyrope;
    public static IMaterial RockSalt;
    public static IMaterial Ruridit;
    public static IMaterial Rubber;
    public static IMaterial Ruby;
    public static IMaterial Salt;
    public static IMaterial Saltpeter;
    public static IMaterial Sapphire;
    public static IMaterial Scheelite;
    public static IMaterial Sodalite;
    public static IMaterial AluminiumSulfite;
    public static IMaterial Tantalite;
    public static IMaterial Coke;


    public static IMaterial SolderingAlloy;
    public static IMaterial Spessartine;
    public static IMaterial Sphalerite;
    public static IMaterial StainlessSteel;
    public static IMaterial Steel;
    public static IMaterial Stibnite;
    public static IMaterial Tetrahedrite;
    public static IMaterial TinAlloy;
    public static IMaterial Topaz;
    public static IMaterial Tungstate;
    public static IMaterial Ultimet;
    public static IMaterial Uraninite;
    public static IMaterial Uvarovite;
    public static IMaterial VanadiumGallium;
    public static IMaterial WroughtIron;
    public static IMaterial Wulfenite;
    public static IMaterial YellowLimonite;
    public static IMaterial YttriumBariumCuprate;
    public static IMaterial NetherQuartz;
    public static IMaterial CertusQuartz;
    public static IMaterial Quartzite;
    public static IMaterial Graphite;
    public static IMaterial Graphene;
    public static IMaterial TungsticAcid;
    public static IMaterial Osmiridium;
    public static IMaterial LithiumChloride;
    public static IMaterial CalciumChloride;
    public static IMaterial Bornite;
    public static IMaterial Chalcocite;

    public static IMaterial GalliumArsenide;
    public static IMaterial Potash;
    public static IMaterial SodaAsh;
    public static IMaterial IndiumGalliumPhosphide;
    public static IMaterial NickelZincFerrite;
    public static IMaterial SiliconDioxide;
    public static IMaterial MagnesiumChloride;
    public static IMaterial SodiumSulfide;
    public static IMaterial PhosphorusPentoxide;
    public static IMaterial Quicklime;
    public static IMaterial SodiumBisulfate;
    public static IMaterial FerriteMixture;
    public static IMaterial Magnesia;
    public static IMaterial PlatinumGroupSludge;
    public static IMaterial Realgar;
    public static IMaterial SodiumBicarbonate;
    public static IMaterial PotassiumDichromate;
    public static IMaterial ChromiumTrioxide;
    public static IMaterial AntimonyTrioxide;
    public static IMaterial Zincite;
    public static IMaterial CupricOxide;
    public static IMaterial CobaltOxide;
    public static IMaterial ArsenicTrioxide;
    public static IMaterial Massicot;
    public static IMaterial Ferrosilite;
    public static IMaterial MetalMixture;
    public static IMaterial SodiumHydroxide;
    public static IMaterial SodiumPersulfate;
    public static IMaterial Bastnasite;
    public static IMaterial Pentlandite;
    public static IMaterial Spodumene;
    public static IMaterial Lepidolite;
    public static IMaterial GlauconiteSand;
    public static IMaterial Malachite;
    public static IMaterial Mica;
    public static IMaterial Barite;
    public static IMaterial Alunite;
    public static IMaterial Talc;
    public static IMaterial Soapstone;
    public static IMaterial Kyanite;
    public static IMaterial IronMagnetic;
    public static IMaterial TungstenCarbide;
    public static IMaterial CarbonDioxide;
    public static IMaterial TitaniumTetrachloride;
    public static IMaterial NitrogenDioxide;
    public static IMaterial HydrogenSulfide;
    public static IMaterial NitricAcid;
    public static IMaterial SulfuricAcid;
    public static IMaterial PhosphoricAcid;
    public static IMaterial SulfurTrioxide;
    public static IMaterial SulfurDioxide;
    public static IMaterial CarbonMonoxide;
    public static IMaterial HypochlorousAcid;
    public static IMaterial Ammonia;
    public static IMaterial HydrofluoricAcid;
    public static IMaterial NitricOxide;
    public static IMaterial Iron3Chloride;
    public static IMaterial UraniumHexafluoride;
    public static IMaterial EnrichedUraniumHexafluoride;
    public static IMaterial DepletedUraniumHexafluoride;
    public static IMaterial NitrousOxide;
    public static IMaterial EnderPearl;
    public static IMaterial PotassiumFeldspar;
    public static IMaterial NeodymiumMagnetic;
    public static IMaterial HydrochloricAcid;
    public static IMaterial Steam;
    public static IMaterial DistilledWater;
    public static IMaterial SodiumPotassium;
    public static IMaterial SamariumMagnetic;
    public static IMaterial ManganesePhosphide;
    public static IMaterial MagnesiumDiboride;
    public static IMaterial MercuryBariumCalciumCuprate;
    public static IMaterial UraniumTriplatinum;
    public static IMaterial SamariumIronArsenicOxide;
    public static IMaterial IndiumTinBariumTitaniumCuprate;
    public static IMaterial UraniumRhodiumDinaquadide;
    public static IMaterial EnrichedNaquadahTriniumEuropiumDuranide;
    public static IMaterial RutheniumTriniumAmericiumNeutronate;
    public static IMaterial PlatinumRaw;
    public static IMaterial InertMetalMixture;
    public static IMaterial RhodiumSulfate;
    public static IMaterial RutheniumTetroxide;
    public static IMaterial OsmiumTetroxide;
    public static IMaterial IridiumChloride;
    public static IMaterial FluoroantimonicAcid;
    public static IMaterial TitaniumTrifluoride;
    public static IMaterial CalciumPhosphide;
    public static IMaterial IndiumPhosphide;
    public static IMaterial BariumSulfide;
    public static IMaterial TriniumSulfide;
    public static IMaterial ZincSulfide;
    public static IMaterial GalliumSulfide;
    public static IMaterial AntimonyTrifluoride;
    public static IMaterial EnrichedNaquadahSulfate;
    public static IMaterial NaquadriaSulfate;
    public static IMaterial Pyrochlore;

    /**
     * Organic chemistry
     */
    public static IMaterial SiliconeRubber;
    public static IMaterial RawRubber;
    public static IMaterial RawStyreneButadieneRubber;
    public static IMaterial StyreneButadieneRubber;
    public static IMaterial PolyvinylAcetate;
    public static IMaterial ReinforcedEpoxyResin;
    public static IMaterial PolyvinylChloride;
    public static IMaterial PolyphenyleneSulfide;
    public static IMaterial GlycerylTrinitrate;
    public static IMaterial Polybenzimidazole;
    public static IMaterial Polydimethylsiloxane;
    public static IMaterial Polyethylene;
    public static IMaterial Epoxy;
    public static IMaterial Polycaprolactam;
    public static IMaterial Polytetrafluoroethylene;
    public static IMaterial Sugar;
    public static IMaterial Methane;
    public static IMaterial Epichlorohydrin;
    public static IMaterial Monochloramine;
    public static IMaterial Chloroform;
    public static IMaterial Cumene;
    public static IMaterial Tetrafluoroethylene;
    public static IMaterial Chloromethane;
    public static IMaterial AllylChloride;
    public static IMaterial Isoprene;
    public static IMaterial Propane;
    public static IMaterial Propene;
    public static IMaterial Ethane;
    public static IMaterial Butene;
    public static IMaterial Butane;
    public static IMaterial DissolvedCalciumAcetate;
    public static IMaterial VinylAcetate;
    public static IMaterial MethylAcetate;
    public static IMaterial Ethenone;
    public static IMaterial Tetranitromethane;
    public static IMaterial Dimethylamine;
    public static IMaterial Dimethylhydrazine;
    public static IMaterial DinitrogenTetroxide;
    public static IMaterial Dimethyldichlorosilane;
    public static IMaterial Styrene;
    public static IMaterial Butadiene;
    public static IMaterial Dichlorobenzene;
    public static IMaterial AceticAcid;
    public static IMaterial Phenol;
    public static IMaterial BisphenolA;
    public static IMaterial VinylChloride;
    public static IMaterial Ethylene;
    public static IMaterial Benzene;
    public static IMaterial Acetone;
    public static IMaterial Glycerol;
    public static IMaterial Methanol;
    public static IMaterial Ethanol;
    public static IMaterial Toluene;
    public static IMaterial DiphenylIsophtalate;
    public static IMaterial PhthalicAcid;
    public static IMaterial Dimethylbenzene;
    public static IMaterial Diaminobenzidine;
    public static IMaterial Dichlorobenzidine;
    public static IMaterial Nitrochlorobenzene;
    public static IMaterial Chlorobenzene;
    public static IMaterial Octane;
    public static IMaterial EthylTertButylEther;
    public static IMaterial Ethylbenzene;
    public static IMaterial Naphthalene;
    public static IMaterial Nitrobenzene;
    public static IMaterial Cyclohexane;
    public static IMaterial NitrosylChloride;
    public static IMaterial CyclohexanoneOxime;
    public static IMaterial Caprolactam;
    public static IMaterial PlatinumSludgeResidue;
    public static IMaterial PalladiumRaw;
    public static IMaterial RarestMetalMixture;
    public static IMaterial AmmoniumChloride;
    public static IMaterial AcidicOsmiumSolution;
    public static IMaterial RhodiumPlatedPalladium;
    public static IMaterial Butyraldehyde;
    public static IMaterial PolyvinylButyral;

    /**
     * Not possible to determine exact Components
     */
    public static IMaterial WoodGas;
    public static IMaterial WoodVinegar;
    public static IMaterial WoodTar;
    public static IMaterial CharcoalByproducts;
    public static IMaterial Biomass;
    public static IMaterial BioDiesel;
    public static IMaterial FermentedBiomass;
    public static IMaterial Creosote;
    public static IMaterial Diesel;
    public static IMaterial RocketFuel;
    public static IMaterial Glue;
    public static IMaterial Lubricant;
    public static IMaterial McGuffium239;
    public static IMaterial IndiumConcentrate;
    public static IMaterial SeedOil;
    public static IMaterial DrillingFluid;
    public static IMaterial ConstructionFoam;

    public static IMaterial Oil;
    public static IMaterial OilHeavy;
    public static IMaterial RawOil;
    public static IMaterial OilLight;
    public static IMaterial NaturalGas;
    public static IMaterial SulfuricHeavyFuel;
    public static IMaterial HeavyFuel;
    public static IMaterial LightlyHydroCrackedHeavyFuel;
    public static IMaterial SeverelyHydroCrackedHeavyFuel;
    public static IMaterial LightlySteamCrackedHeavyFuel;
    public static IMaterial SeverelySteamCrackedHeavyFuel;
    public static IMaterial SulfuricLightFuel;
    public static IMaterial LightFuel;
    public static IMaterial LightlyHydroCrackedLightFuel;
    public static IMaterial SeverelyHydroCrackedLightFuel;
    public static IMaterial LightlySteamCrackedLightFuel;
    public static IMaterial SeverelySteamCrackedLightFuel;
    public static IMaterial SulfuricNaphtha;
    public static IMaterial Naphtha;
    public static IMaterial LightlyHydroCrackedNaphtha;
    public static IMaterial SeverelyHydroCrackedNaphtha;
    public static IMaterial LightlySteamCrackedNaphtha;
    public static IMaterial SeverelySteamCrackedNaphtha;
    public static IMaterial SulfuricGas;
    public static IMaterial RefineryGas;
    public static IMaterial LightlyHydroCrackedGas;
    public static IMaterial SeverelyHydroCrackedGas;
    public static IMaterial LightlySteamCrackedGas;
    public static IMaterial SeverelySteamCrackedGas;
    public static IMaterial HydroCrackedEthane;
    public static IMaterial HydroCrackedEthylene;
    public static IMaterial HydroCrackedPropene;
    public static IMaterial HydroCrackedPropane;
    public static IMaterial HydroCrackedButane;
    public static IMaterial HydroCrackedButene;
    public static IMaterial HydroCrackedButadiene;
    public static IMaterial SteamCrackedEthane;
    public static IMaterial SteamCrackedEthylene;
    public static IMaterial SteamCrackedPropene;
    public static IMaterial SteamCrackedPropane;
    public static IMaterial SteamCrackedButane;
    public static IMaterial SteamCrackedButene;
    public static IMaterial SteamCrackedButadiene;
    public static IMaterial LPG;

    public static IMaterial RawGrowthMedium;
    public static IMaterial SterileGrowthMedium;
    public static IMaterial Bacteria;
    public static IMaterial BacterialSludge;
    public static IMaterial EnrichedBacterialSludge;
    public static IMaterial Mutagen;
    public static IMaterial GelatinMixture;
    public static IMaterial RawGasoline;
    public static IMaterial Gasoline;
    public static IMaterial HighOctaneGasoline;
    public static IMaterial CoalGas;
    public static IMaterial CoalTar;
    public static IMaterial Gunpowder;
    public static IMaterial Oilsands;
    public static IMaterial RareEarth;
    public static IMaterial Stone;
    public static IMaterial Lava;
    public static IMaterial Glowstone;
    public static IMaterial NetherStar;
    public static IMaterial Endstone;
    public static IMaterial Netherrack;
    public static IMaterial CetaneBoostedDiesel;
    public static IMaterial Collagen;
    public static IMaterial Gelatin;
    public static IMaterial Agar;
    public static IMaterial Andesite;
    public static IMaterial Milk;
    public static IMaterial Cocoa;
    public static IMaterial Wheat;
    public static IMaterial Meat;
    public static IMaterial Wood;
    public static IMaterial TreatedWood;
    public static IMaterial Paper;
    public static IMaterial FishOil;
    public static IMaterial RubySlurry;
    public static IMaterial SapphireSlurry;
    public static IMaterial GreenSapphireSlurry;
    public static IMaterial DyeBlack;
    public static IMaterial DyeRed;
    public static IMaterial DyeGreen;
    public static IMaterial DyeBrown;
    public static IMaterial DyeBlue;
    public static IMaterial DyePurple;
    public static IMaterial DyeCyan;
    public static IMaterial DyeLightGray;
    public static IMaterial DyeGray;
    public static IMaterial DyePink;
    public static IMaterial DyeLime;
    public static IMaterial DyeYellow;
    public static IMaterial DyeLightBlue;
    public static IMaterial DyeMagenta;
    public static IMaterial DyeOrange;
    public static IMaterial DyeWhite;

    public static IMaterial ImpureEnrichedNaquadahSolution;
    public static IMaterial EnrichedNaquadahSolution;
    public static IMaterial AcidicEnrichedNaquadahSolution;
    public static IMaterial EnrichedNaquadahWaste;
    public static IMaterial ImpureNaquadriaSolution;
    public static IMaterial NaquadriaSolution;
    public static IMaterial AcidicNaquadriaSolution;
    public static IMaterial NaquadriaWaste;
    public static IMaterial Lapotron;
    public static IMaterial UUMatter;

    /**
     * Second Degree Compounds
     */
    public static IMaterial Glass;
    public static IMaterial Perlite;
    public static IMaterial Borax;
    public static IMaterial Olivine;
    public static IMaterial Opal;
    public static IMaterial Amethyst;
    public static IMaterial Lapis;
    public static IMaterial Blaze;
    public static IMaterial Apatite;
    public static IMaterial BlackSteel;
    public static IMaterial DamascusSteel;
    public static IMaterial TungstenSteel;
    public static IMaterial CobaltBrass;
    public static IMaterial TricalciumPhosphate;
    public static IMaterial GarnetRed;
    public static IMaterial GarnetYellow;
    public static IMaterial Marble;
    public static IMaterial GraniteBlack;
    public static IMaterial GraniteRed;
    public static IMaterial VanadiumMagnetite;
    public static IMaterial QuartzSand;
    public static IMaterial Pollucite;
    public static IMaterial Bentonite;
    public static IMaterial FullersEarth;
    public static IMaterial Pitchblende;
    public static IMaterial Monazite;
    public static IMaterial Mirabilite;
    public static IMaterial Trona;
    public static IMaterial Gypsum;
    public static IMaterial Zeolite;
    public static IMaterial Concrete;
    public static IMaterial SteelMagnetic;
    public static IMaterial VanadiumSteel;
    public static IMaterial Potin;
    public static IMaterial BorosilicateGlass;
    public static IMaterial NaquadahAlloy;
    public static IMaterial SulfuricNickelSolution;
    public static IMaterial SulfuricCopperSolution;
    public static IMaterial LeadZincSolution;
    public static IMaterial NitrationMixture;
    public static IMaterial DilutedSulfuricAcid;
    public static IMaterial DilutedHydrochloricAcid;
    public static IMaterial Flint;
    public static IMaterial Air;
    public static IMaterial LiquidAir;
    public static IMaterial NetherAir;
    public static IMaterial LiquidNetherAir;
    public static IMaterial EnderAir;
    public static IMaterial LiquidEnderAir;
    public static IMaterial AquaRegia;
    public static IMaterial SaltWater;
    public static IMaterial Clay;
    public static IMaterial Redstone;

    /**
     * Third Degree IMaterials
     */
    public static IMaterial Electrotine;
    public static IMaterial EnderEye;
    public static IMaterial Diatomite;
    public static IMaterial RedSteel;
    public static IMaterial BlueSteel;
    public static IMaterial Basalt;
    public static IMaterial GraniticMineralSand;
    public static IMaterial Redrock;
    public static IMaterial GarnetSand;
    public static IMaterial HSSG;
    public static IMaterial IridiumMetalResidue;
    public static IMaterial Granite;
    public static IMaterial Brick;
    public static IMaterial Fireclay;
    public static IMaterial Diorite;

    /**
     * Fourth Degree IMaterials
     */
    public static IMaterial RedAlloy;
    public static IMaterial BlueAlloy;
    public static IMaterial BasalticMineralSand;
    public static IMaterial HSSE;
    public static IMaterial HSSS;
}
