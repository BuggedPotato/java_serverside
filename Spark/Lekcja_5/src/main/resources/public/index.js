function init()
{
    document.getElementById( "btn-add" ).addEventListener( "click", addCar );
}

async function addCar()
{
    let model = document.getElementById( "model" ).value;
    if( model === "" )
        return;
    let year = document.getElementById( "year" ).value;
    let airbags = [...document.getElementsByClassName( "airbag" )].map( ( input, i )=>{
        return {
            name: input.id,
            value: input.checked
        };
    } );
    let colour = document.getElementById( "colour" ).value;

    const carObj = { model: model, airbags: airbags, year: year, colour: colour };
    let json = await asyncFetchJSON( "/add", carObj );
    console.log(json);
    alert( JSON.stringify( json, null, 5 ) );
}

async function asyncFetchJSON( target, data )
{
    const options = {
        method: "POST",
        body: JSON.stringify( data )
    };
    let res = await fetch( target, options );
    if( !res.ok )
        return res.status;
    return await res.json();
}